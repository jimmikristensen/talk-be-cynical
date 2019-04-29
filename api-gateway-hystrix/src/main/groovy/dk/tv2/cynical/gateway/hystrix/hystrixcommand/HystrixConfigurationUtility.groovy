package dk.tv2.cynical.gateway.hystrix.hystrixcommand

import com.netflix.hystrix.HystrixCommandProperties
import com.netflix.hystrix.HystrixThreadPoolProperties

class HystrixConfigurationUtility {

    static HystrixCommandProperties.Setter createHystrixCommandPropertiesSetter() {
        HystrixCommandProperties.invokeMethod("Setter", null)
    }

    static HystrixThreadPoolProperties.Setter createHystrixThreadPoolPropertiesSetter() {
        HystrixThreadPoolProperties.invokeMethod("Setter", null)
    }
}
