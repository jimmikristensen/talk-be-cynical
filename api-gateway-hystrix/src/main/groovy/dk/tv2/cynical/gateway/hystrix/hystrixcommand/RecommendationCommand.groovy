package dk.tv2.cynical.gateway.hystrix.hystrixcommand

import com.netflix.hystrix.*
import dk.tv2.cynical.gateway.hystrix.httpclient.RecommendationsClient
import dk.tv2.cynical.gateway.hystrix.model.Content
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException

import javax.inject.Inject
import java.util.concurrent.CompletableFuture

class RecommendationCommand extends HystrixCommand<Content[]> {

    @Client("http://localhost:8091")
    @Inject RxHttpClient client

    static final String CMD_NAME = "RecommendationCommand"
    static final int THREAD_POOL_SIZE = 15
    static final int EXEC_TIMEOUT = 1500

    def limit
    RecommendationsClient recommendationsClient

    RecommendationCommand(def limit, RecommendationsClient recommendationsClient) {
        super(Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey(CMD_NAME+"-Pool"))
                .andCommandKey(HystrixCommandKey.Factory.asKey(CMD_NAME))
                .andCommandPropertiesDefaults(
                HystrixConfigurationUtility.createHystrixCommandPropertiesSetter()
                        .withExecutionTimeoutInMilliseconds(EXEC_TIMEOUT))
                .andThreadPoolPropertiesDefaults(
                HystrixConfigurationUtility.createHystrixThreadPoolPropertiesSetter()
                        .withCoreSize(THREAD_POOL_SIZE)))

        this.limit = limit
        this.recommendationsClient = recommendationsClient
    }

    @Override
    protected Content[] run() throws Exception {

        if (limit in Integer) {
            return recommendationsClient.fetchRecommendations(limit as int)
        } else {
            return recommendationsClient.fetchRecommendations()
        }

    }

    @Override
    protected Content[] getFallback() {
        return []
    }
}
