package dk.tv2.cynical.content

import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.api.sync.RedisCommands
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.inject.Singleton

@Singleton
class ContentRepository {

    private static final Logger LOG = LoggerFactory.getLogger(ContentRepository.class)
    private final StatefulRedisConnection<String, String> redisConnection

    ContentRepository(StatefulRedisConnection<String, String> redisConnection) {
        this.redisConnection = redisConnection
    }

    List<VideoMetadata> fetchVideoMetadata() {
        final RedisCommands<String, String> syncCommands = redisConnection.sync()
        def metadataResults = []

        ['con:meta1', 'con:meta2', 'con:meta3', 'con:meta5', 'con:meta8'].each {
            def map = [:]
            syncCommands.hmget(it, 'type', 'title', 'desc', 'imgUrl', 'numEp', 'numSeasons', 'genre').each {
                try {
                    map.put(it.key, it.value)
                } catch (NoSuchElementException e) {
                    // skip the element if it does not exist in redis
                }
            }

            def type = map.get('type') ?: 'Unknown'
            def title = map.get('title') ?: 'Unknown Title'
            def desc = map.get('desc') ?: ''
            def img = map.get('imgUrl')
            def numEp
            def numSeasons
            def genre = map.get('genre') ?: null

            if (map.get('numEp')) {
                numEp = (map.get('numEp') as String).isInteger() ? map.get('numEp') as Integer : 1
            }
            if (map.get('numSeasons')) {
                numSeasons = (map.get('numSeasons') as String).isInteger() ? map.get('numSeasons') as Integer : 1
            }

            metadataResults << new VideoMetadata(type: type, title: title, description: desc, imageURL: img, numEpisodes: numEp, numSeasons: numSeasons, genre: genre)
        }

        return metadataResults
    }
}
