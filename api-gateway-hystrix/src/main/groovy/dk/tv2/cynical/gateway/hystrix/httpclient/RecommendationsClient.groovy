package dk.tv2.cynical.gateway.hystrix.httpclient

import dk.tv2.cynical.gateway.hystrix.model.Content
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client

import javax.annotation.Nullable

// Declarative client for downstream recommendation service

@Client("http://\${micronaut.application.recommendation.hostname}:\${micronaut.application.recommendation.port}")
interface RecommendationsClient {

    @Get('/recommendations')
    Content[] fetchRecommendations()

    @Get('/recommendations{?limit}')
    Content[] fetchRecommendations(@Nullable int limit)
}
