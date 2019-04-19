package dk.tv2.cynical.gateway.datafetcher

import dk.tv2.cynical.gateway.httpclient.ContentClient
import dk.tv2.cynical.gateway.model.Content

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import groovy.transform.CompileStatic

import javax.inject.Singleton
import java.util.concurrent.CompletableFuture

@Singleton
@CompileStatic
class ContentDataFetcher implements DataFetcher<CompletableFuture<Content[]>> {

    private final ContentClient contentClient

    ContentDataFetcher(ContentClient contentClient) {
        this.contentClient = contentClient
    }

    @Override
    CompletableFuture<Content[]> get(DataFetchingEnvironment environment) throws Exception {
        def limitArg = environment.getArgument('limit')
        def keyArg = environment.getArgument('key')

        if (limitArg in Integer) {
            return contentClient.fetchContent(limitArg as int)
        } else if (keyArg != null) {
            return contentClient.fetchContent(keyArg as String)
        } else {
            return contentClient.fetchContent()
        }
    }

}
