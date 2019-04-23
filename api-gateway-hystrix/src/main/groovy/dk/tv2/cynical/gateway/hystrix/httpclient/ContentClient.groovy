package dk.tv2.cynical.gateway.hystrix.httpclient

import dk.tv2.cynical.gateway.hystrix.model.Content
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client

import javax.annotation.Nullable
import java.util.concurrent.CompletableFuture

@Client('http://localhost:8081')
interface ContentClient {

    @Get('/content')
    CompletableFuture<Content[]> fetchContent()

    @Get('/content{?limit}')
    CompletableFuture<Content[]> fetchContent(@Nullable int limit)

    @Get('/content/{key}')
    CompletableFuture<Content[]> fetchContent(String key)
}
