package dk.tv2.cynical.gateway.httpclient

import dk.tv2.cynical.gateway.model.Content
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client

import javax.annotation.Nullable
import java.util.concurrent.CompletableFuture

@Client('http://localhost:8082')
interface RecommendationsClient {

    @Get('/recommendations')
    CompletableFuture<Content[]> fetchRecommendations()

    @Get('/recommendations{?limit}')
    CompletableFuture<Content[]> fetchRecommendations(@Nullable int limit)
}
