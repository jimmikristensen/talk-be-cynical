package dk.tv2.cynical.gateway.datafetcher

import dk.tv2.cynical.gateway.httpclient.RecommendationsClient
import dk.tv2.cynical.gateway.model.Content
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
    CompletableFuture<Content> get(DataFetchingEnvironment environment) throws Exception {
//        String name = environment.getArgument("name")
        return recommendationsClient.fetchRecommendation()

    }

}
