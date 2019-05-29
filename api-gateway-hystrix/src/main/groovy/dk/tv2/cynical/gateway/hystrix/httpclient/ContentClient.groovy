package dk.tv2.cynical.gateway.hystrix.httpclient

import dk.tv2.cynical.gateway.hystrix.model.Content
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client

import javax.annotation.Nullable

// Declarative client for downstream content service

@Client("http://\${micronaut.application.content.hostname}:\${micronaut.application.content.port}")
interface ContentClient {

    @Get('/content')
    Content[] fetchContent()

    @Get('/content{?limit}')
    Content[] fetchContent(@Nullable int limit)

    @Get('/content/{key}')
    Content[] fetchContent(String key)
}
