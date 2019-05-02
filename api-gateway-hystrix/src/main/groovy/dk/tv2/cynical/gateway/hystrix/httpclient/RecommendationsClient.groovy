package dk.tv2.cynical.gateway.hystrix.httpclient

import dk.tv2.cynical.gateway.hystrix.model.Content
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client

import javax.annotation.Nullable

@Client(value = 'http://localhost:8091') // mountebank
//@Client(value = 'http://localhost:8082') // real service
interface RecommendationsClient {

    @Get('/recommendations')
    Content[] fetchRecommendations()

    @Get('/recommendations{?limit}')
    Content[] fetchRecommendations(@Nullable int limit)
}
