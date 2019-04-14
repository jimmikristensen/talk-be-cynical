package dk.tv2.cynical.gateway.httpclient

import dk.tv2.cynical.gateway.model.Content

import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.reactivex.Maybe

@Client('http://localhost:8081')
interface ContentClient {

    @Get('/content')
    Maybe<List<Content>> fetchContent()
}
