package dk.tv2.cynical.gateway.httpclient

import dk.tv2.cynical.gateway.model.Recommendation
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client

import java.util.concurrent.CompletableFuture

@Client('http://localhost:8091')
interface RecommendationsClient {

    @Get('/recommendations')
    CompletableFuture<Recommendation> fetchRecommendation()
}
