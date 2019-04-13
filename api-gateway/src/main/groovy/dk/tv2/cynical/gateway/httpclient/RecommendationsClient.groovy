package dk.tv2.cynical.gateway.httpclient

import dk.tv2.cynical.gateway.model.Recommendation
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.reactivex.Maybe

@Client('http://localhost:8082')
interface RecommendationsClient {

    @Get('/recommendations')
    Maybe<Recommendation> fetchRecommendation()
}
