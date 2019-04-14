package dk.tv2.cynical.gateway.datafetcher

import dk.tv2.cynical.gateway.httpclient.RecommendationsClient
import dk.tv2.cynical.gateway.model.Recommendation
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import groovy.transform.CompileStatic
import io.reactivex.Single

import javax.inject.Singleton

@Singleton
@CompileStatic
class RecommendationDataFetcher implements DataFetcher<Recommendation> {

    private final RecommendationsClient recommendationsClient

    RecommendationDataFetcher(RecommendationsClient recommendationsClient) {
        this.recommendationsClient = recommendationsClient
    }

    @Override
    Recommendation get(DataFetchingEnvironment environment) throws Exception {
        String name = environment.getArgument("name")
        name = name?.trim() ?: "World"
        def t = "none"

//        return contentClient.fetchRecommendation().toSingle()

        println "called"
//        println environment

//        return ['test1','test2']

//        def rec = new Recommendation(api: "testing", type: "some type")


        return recommendationsClient.fetchRecommendation().blockingGet()

//        contentClient.fetchRecommendation().toSingle().subscribe(
//            {
//                Recommendation rec -> return rec.type
//            },
//            {
//                throwable -> throwable.printStackTrace()
//            }
//        )


//        return "test"

    }

    void testing(Recommendation test) {
        println test.type
    }
}
