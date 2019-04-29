package dk.tv2.cynical.gateway.hystrix.datafetcher

import com.netflix.hystrix.HystrixCommand
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

    RecommendationDataFetcher(RecommendationsClient recommendationsClient) {
        this.recommendationsClient = recommendationsClient
    }

    @Override
    Content[] get(DataFetchingEnvironment environment) throws Exception {
        def limitArg = environment.getArgument('limit')

        HystrixCommand<Content[]> command = new RecommendationCommand(limitArg, recommendationsClient)
        return command.execute()
    }

}
