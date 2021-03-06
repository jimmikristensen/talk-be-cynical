package dk.tv2.cynical.recommendations

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue

import javax.annotation.Nullable

import static io.micronaut.http.HttpResponse.*


@Controller("/recommendations")
class RecommendationsController {

    private final RecommendationsRepository recommendationsRepository

    RecommendationsController(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository
    }

    @Get("{?limit}")
    HttpResponse<String> index(@QueryValue('limit') @Nullable Integer limit) {
        return ok().body(recommendationsRepository.fetchRecommendation(limit))
    }

}