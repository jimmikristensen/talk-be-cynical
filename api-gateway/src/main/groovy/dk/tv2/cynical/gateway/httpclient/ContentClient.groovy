package dk.tv2.cynical.gateway.httpclient

import dk.tv2.cynical.gateway.model.Content

import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client

import java.util.concurrent.CompletableFuture

@Client('http://localhost:8081')
interface ContentClient {

    @Get('/content')
    CompletableFuture<Content[]> fetchContent()

    @Get('/content/{limit}')
    CompletableFuture<Content[]> fetchContent(int limit)
}
