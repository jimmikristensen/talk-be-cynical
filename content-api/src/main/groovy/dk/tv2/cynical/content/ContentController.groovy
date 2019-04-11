package dk.tv2.cynical.content

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import static io.micronaut.http.HttpResponse.*


@Controller("/content")
class ContentController {

    private final ContentRepository contentRepository

    ContentController(ContentRepository contentRepository) {
        this.contentRepository = contentRepository
    }

    @Get("/")
    HttpResponse<String> index() {
        return ok().body(contentRepository.fetchVideoMetadata())
    }
}