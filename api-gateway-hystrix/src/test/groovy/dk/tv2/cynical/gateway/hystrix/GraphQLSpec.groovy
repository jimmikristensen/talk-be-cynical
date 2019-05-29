package dk.tv2.cynical.gateway.hystrix

import dk.tv2.cynical.gateway.hystrix.graphql.GraphQLFactory
import io.micronaut.configuration.graphql.GraphQLResponseBody
import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.PropertySource
import io.micronaut.core.util.CollectionUtils
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.server.EmbeddedServer
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.GenericContainer
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import javax.annotation.Nullable

class GraphQLSpec extends Specification {

    @AutoCleanup
    EmbeddedServer embeddedServer

    GraphQLClient graphQLClient

    @Shared
    def mountebankContainer = new GenericContainer("expert360/mountebank")
            .withExposedPorts(2525, 8090)
            .withClasspathResourceMapping("./imposters", "/imposters", BindMode.READ_WRITE)
            .withCommand("--configfile /imposters/imposters.ejs --allowInjection")

    def setup() {
        mountebankContainer.start()

        embeddedServer = ApplicationContext.run(
                EmbeddedServer,
                PropertySource.of(
                        "test",
                        CollectionUtils.mapOf(
                                "micronaut.application.content.port", mountebankContainer.getMappedPort(8090)
                        )
                ),
                "test"
        )

        embeddedServer.applicationContext.registerSingleton(GraphQLFactory)
        graphQLClient = embeddedServer.applicationContext.getBean(GraphQLClient)
    }

    void "querying for id with limit 1 returns single content object"() {
        given:
        String query = "query t { editorialContent(limit:1) { id } }"

        when:
        GraphQLResponseBody response = graphQLClient.post(query, null, null)
        List<String> content = response.getSpecification()["data"]["editorialContent"]

        then:
        content.size() == 1

        and:
        content["id"] == ["content:series-10"]
    }

    @Unroll
    void "result on index #index is a #assetType with id #assetId"() {
        given:
        String query = "query t {" +
                "  editorialContent(limit:3) {" +
                "    id" +
                "    api" +
                "    description" +
                "    imageURL" +
                "    title" +
                "    type" +
                "    ... on Episode {" +
                "      episode" +
                "    }" +
                "    ... on Series {" +
                "      numEpisodes" +
                "      numSeasons" +
                "    }" +
                "    ... on Movie {" +
                "      genre" +
                "    }" +
                "  }" +
                "}"
        GraphQLResponseBody response = graphQLClient.post(query, null, null)
        List<String> content = response.getSpecification()["data"]["editorialContent"]

        expect:
        content.size() == 3
        content.get(index).id == assetId
        content.get(index).title == assetTitle
        content.get(index).type == assetType
        content.get(index).genre == assetGenre

        where:
        index   || assetId              | assetTitle            | assetType | assetGenre
        0       || 'content:series-10'  | 'Løb som en pige'     | 'Series'  | null
        1       || 'content:series-5'   | 'Station 2: Fængslet' | 'Series'  | null
        2       || 'content:movie-1'    | 'Alle for én'         | 'Movie'   | 'Komedie'

    }

    void "when receiving a timeout, hystrix returns a fallback content object"() {
        given:
        // Mountebank stub will wait 10000 ms causing a timeout
        String query = "query t { editorialContent(limit:4) { id isResponseFromFallback } }"

        when:
        GraphQLResponseBody response = graphQLClient.post(query, null, null)
        List<String> content = response.getSpecification()["data"]["editorialContent"]

        then:
        content.size() == 1

        and:
        content.first().id == '123-fallback'
        content.first().isResponseFromFallback == true
    }

    void "when receiving content-type: text/plan from content service hystrix will run fallback"() {
        given:
        // Mountebank stub will return Content-Type: text/plain with a json body
        String query = "query t { editorialContent(limit:5) { id isResponseFromFallback } }"

        when:
        GraphQLResponseBody response = graphQLClient.post(query, null, null)
        List<String> content = response.getSpecification()["data"]["editorialContent"]

        then:
        content.size() == 1

        and:
        content.first().isResponseFromFallback == true
    }

    void "when receiving malformed json hystrix will fallback and return fallback object"() {
        given:
        // Mountebank stub will return regular sentence instead of json formatted data in the body
        String query = "query t { editorialContent(limit:6) { id isResponseFromFallback } }"

        when:
        GraphQLResponseBody response = graphQLClient.post(query, null, null)
        List<String> content = response.getSpecification()["data"]["editorialContent"]

        then:
        content.size() == 1

        and:
        content.first().isResponseFromFallback == true
    }

    @Client("/graphql")
    static interface GraphQLClient {
        @Post(value = "{?query,operationName,variables}")
        GraphQLResponseBody post(@QueryValue @Nullable String query, @QueryValue @Nullable String operationName, @QueryValue @Nullable String variables)
    }


}
