package dk.tv2.cynical.content

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue

import javax.annotation.Nullable

import static io.micronaut.http.HttpResponse.*

@Controller("/content")
class ContentController {

    private final ContentRepository contentRepository

    ContentController(ContentRepository contentRepository) {
        this.contentRepository = contentRepository
    }

    @Get("{?limit}")
    HttpResponse<String> index(@QueryValue('limit') @Nullable Integer limit) {
        println "LIMIT ENDPOINT"
        return ok().body(contentRepository.fetchVideoMetadataWithLimit(limit))
    }

    @Get("/{key}")
    HttpResponse<String> index(String key) {
        println "KEY ENDPOINT"
        return ok().body(contentRepository.fetchVideoMetadataByKey(key))
    }
}