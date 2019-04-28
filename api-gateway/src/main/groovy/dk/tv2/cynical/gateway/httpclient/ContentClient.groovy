package dk.tv2.cynical.gateway.httpclient

import dk.tv2.cynical.gateway.model.Content

import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client

import javax.annotation.Nullable
import java.util.concurrent.CompletableFuture

@Client('http://contentapi:8080')
interface ContentClient {

    @Get('/content')
    CompletableFuture<Content[]> fetchContent()

    @Get('/content{?limit}')
    CompletableFuture<Content[]> fetchContent(@Nullable int limit)

    @Get('/content/{key}')
    CompletableFuture<Content[]> fetchContent(String key)
}
