package dk.tv2.cynical.gateway.hystrix.httpclient

import dk.tv2.cynical.gateway.hystrix.model.Content
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client

import javax.annotation.Nullable

@Client('http://localhost:8090')
interface ContentClient {

    @Get('/content')
    Content[] fetchContent()

    @Get('/content{?limit}')
    Content[] fetchContent(@Nullable int limit)

    @Get('/content/{key}')
    Content[] fetchContent(String key)
}
