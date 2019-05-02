package dk.tv2.cynical.gateway.hystrix.datafetcher

import com.netflix.hystrix.HystrixCommand
import dk.tv2.cynical.gateway.hystrix.httpclient.ContentClient
import dk.tv2.cynical.gateway.hystrix.hystrixcommand.ContentCommand
import dk.tv2.cynical.gateway.hystrix.model.Content
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import groovy.transform.CompileStatic

import javax.inject.Singleton

@Singleton
@CompileStatic
class ContentDataFetcher implements DataFetcher<Content[]> {

    private final ContentClient contentClient

    ContentDataFetcher(ContentClient contentClient) {
        this.contentClient = contentClient
    }

    @Override
    Content[] get(DataFetchingEnvironment environment) throws Exception {
        def limitArg = environment.getArgument('limit')
        def keyArg = environment.getArgument('key')

        HystrixCommand<Content[]> command = new ContentCommand(limitArg, keyArg, contentClient)
        def contentList = command.execute()

        contentList.each {
            it.isCircuitBreakerOpen = command.isCircuitBreakerOpen()
            it.isResponseFromFallback = command.isResponseFromFallback()
            it.isResponseShortCircuited = command.isResponseShortCircuited()
        }

        return contentList
    }

}
