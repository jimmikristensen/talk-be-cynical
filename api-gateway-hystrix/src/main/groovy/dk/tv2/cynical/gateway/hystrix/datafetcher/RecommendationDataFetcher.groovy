package dk.tv2.cynical.gateway.hystrix.datafetcher

import com.netflix.hystrix.HystrixCommand
import dk.tv2.cynical.gateway.hystrix.httpclient.ContentClient
import dk.tv2.cynical.gateway.hystrix.httpclient.RecommendationsClient
import dk.tv2.cynical.gateway.hystrix.hystrixcommand.RecommendationCommand
import dk.tv2.cynical.gateway.hystrix.model.Content
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import groovy.transform.CompileStatic

import javax.inject.Singleton

@Singleton
@CompileStatic
class RecommendationDataFetcher implements DataFetcher<Content[]> {

    private final RecommendationsClient recommendationsClient
    private final ContentClient contentClient

    RecommendationDataFetcher(RecommendationsClient recommendationsClient, ContentClient contentClient) {
        this.recommendationsClient = recommendationsClient
        this.contentClient = contentClient
    }

    @Override
    Content[] get(DataFetchingEnvironment environment) throws Exception {
        def limitArg = environment.getArgument('limit')

        HystrixCommand<Content[]> command = new RecommendationCommand(limitArg, recommendationsClient, contentClient)
        def contentList = command.execute()

        contentList.each {
            it.isCircuitBreakerOpen = command.isCircuitBreakerOpen()
            it.isResponseFromFallback = command.isResponseFromFallback()
            it.isResponseShortCircuited = command.isResponseShortCircuited()
        }

        return contentList
    }

}
