package dk.tv2.cynical.gateway.hystrix.hystrixcommand

import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandGroupKey
import com.netflix.hystrix.HystrixCommandKey
import dk.tv2.cynical.gateway.hystrix.httpclient.ContentClient
import dk.tv2.cynical.gateway.hystrix.model.Content

class ContentCommand extends HystrixCommand<Content[]> {

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
        if (limitArg in Integer) {
            return contentClient.fetchContent(limitArg as int)
        } else if (keyArg != null) {
            return contentClient.fetchContent(keyArg as String)
        } else {
            return contentClient.fetchContent()
        }
    }

    @Override
    protected Content[] getFallback() {
        return []
    }

}
