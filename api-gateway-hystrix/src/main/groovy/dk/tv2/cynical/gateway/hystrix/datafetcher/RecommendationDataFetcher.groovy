package dk.tv2.cynical.gateway.hystrix.datafetcher

import dk.tv2.cynical.gateway.hystrix.httpclient.RecommendationsClient
import dk.tv2.cynical.gateway.hystrix.model.Content
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import groovy.transform.CompileStatic

import javax.inject.Singleton
import java.util.concurrent.CompletableFuture

@Singleton
@CompileStatic
class RecommendationDataFetcher implements DataFetcher<CompletableFuture<Content>> {

    private final RecommendationsClient recommendationsClient

    RecommendationDataFetcher(RecommendationsClient recommendationsClient) {
        this.recommendationsClient = recommendationsClient
    }

    @Override
    CompletableFuture<Content[]> get(DataFetchingEnvironment environment) throws Exception {
        def limitArg = environment.getArgument('limit')

        if (limitArg in Integer) {
            return recommendationsClient.fetchRecommendations(limitArg as int)
        } else {
            return recommendationsClient.fetchRecommendations()
        }

    }

}
