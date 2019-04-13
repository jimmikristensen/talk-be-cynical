package dk.tv2.cynical.gateway.datafetcher

import dk.tv2.cynical.gateway.httpclient.RecommendationsClient
import dk.tv2.cynical.gateway.model.Recommendation
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import groovy.transform.CompileStatic

import javax.inject.Singleton

@Singleton
@CompileStatic
class HelloDataFetcher implements DataFetcher<String> {

    private final RecommendationsClient recommendationsClient

    HelloDataFetcher(RecommendationsClient recommendationsClient) {
        this.recommendationsClient = recommendationsClient
    }

    @Override
    String get(DataFetchingEnvironment environment) throws Exception {
        String name = environment.getArgument("name")
        name = name?.trim() ?: "World"

        recommendationsClient.fetchRecommendation().toSingle().subscribe(
            {
                Recommendation rec -> testing(rec)
            },
            {

            }
        )



        return "Hello ${name}!"
    }

    void testing(Recommendation test) {
        println test.type
    }
}
