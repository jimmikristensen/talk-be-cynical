package dk.tv2.cynical.gateway.hystrix.hystrixcommand

import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandGroupKey
import com.netflix.hystrix.HystrixCommandKey
import dk.tv2.cynical.gateway.hystrix.httpclient.ContentClient
import dk.tv2.cynical.gateway.hystrix.model.Content
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ContentCommand extends HystrixCommand<Content[]> {

    private static final Logger LOG = LoggerFactory.getLogger(ContentCommand.class)

    static final String CMD_NAME = "ContentCommand"
    static final int THREAD_POOL_SIZE = 15
    static final int EXEC_TIMEOUT = 1500

    def limitArg
    def keyArg
    ContentClient contentClient

    ContentCommand(def limitArg, def keyArg, ContentClient contentClient) {
        super(Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey(CMD_NAME+"-Pool"))
                .andCommandKey(HystrixCommandKey.Factory.asKey(CMD_NAME))
                .andCommandPropertiesDefaults(
                    HystrixConfigurationUtility.createHystrixCommandPropertiesSetter()
                            .withExecutionTimeoutInMilliseconds(EXEC_TIMEOUT))
                .andThreadPoolPropertiesDefaults(
                    HystrixConfigurationUtility.createHystrixThreadPoolPropertiesSetter()
                            .withCoreSize(THREAD_POOL_SIZE)))

        this.limitArg = limitArg
        this.keyArg = keyArg
        this.contentClient = contentClient
    }

    @Override
    protected Content[] run() throws Exception {
        Content[] content

        if (limitArg in Integer) {
            content = contentClient.fetchContent(limitArg as int)
        } else if (keyArg != null) {
            content = contentClient.fetchContent(keyArg as String)
        } else {
            content = contentClient.fetchContent()
        }

        // If returned content is null, it means the result set could not be deserialized
        // it might be because of wrong mime type returned by content server
        if (content.first() == null) {
            throw new NullPointerException('Unable to deserialize null object from content server')
        }

        return content
    }

    @Override
    protected Content[] getFallback() {
        LOG.warn('Content API Fallback Executed')
        return [new Content(
                id: '123-fallback',
                api: 'content-api',
                type: 'episode',
                title: 'Fallback',
                description: 'This is a fallback episode, you could use anything e.g. cached content',
                imageURL: 'no image',
                episode: 1,
                season: 1
        )]
    }

}
