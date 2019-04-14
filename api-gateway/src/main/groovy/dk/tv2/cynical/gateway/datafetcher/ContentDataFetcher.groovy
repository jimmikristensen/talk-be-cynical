package dk.tv2.cynical.gateway.datafetcher

import dk.tv2.cynical.gateway.httpclient.ContentClient
import dk.tv2.cynical.gateway.model.Content

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import groovy.transform.CompileStatic

import javax.inject.Singleton

@Singleton
@CompileStatic
class ContentDataFetcher implements DataFetcher<Content> {

    private final ContentClient contentClient

    ContentDataFetcher(ContentClient contentClient) {
        this.contentClient = contentClient
    }

    @Override
    Content get(DataFetchingEnvironment environment) throws Exception {
        String name = environment.getArgument("name")
        name = name?.trim() ?: "World"
        def t = "none"

//        return contentClient.fetchRecommendation().toSingle()

        println "called"
//        println environment

//        return ['test1','test2']

//        def rec = new Recommendation(api: "testing", type: "some type")


        List<Content> contentList = contentClient.fetchContent().blockingGet()
        return contentList.get(0)

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

}
