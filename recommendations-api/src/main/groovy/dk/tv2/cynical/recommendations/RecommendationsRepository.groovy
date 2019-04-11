package dk.tv2.cynical.recommendations

import io.lettuce.core.api.sync.RedisCommands
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import io.lettuce.core.api.StatefulRedisConnection

import javax.inject.Singleton

@Singleton
class RecommendationsRepository {

    private static final Logger LOG = LoggerFactory.getLogger(RecommendationsRepository.class)
    private final StatefulRedisConnection<String, String> redisConnection

    RecommendationsRepository(StatefulRedisConnection<String, String> redisConnection) {
        this.redisConnection = redisConnection
    }

    SeriesRecommendation fetchRecommendation() {
        def map = [:]

        final RedisCommands<String, String> syncCommands = redisConnection.sync()
        def randomKey = syncCommands.randomkey()
        syncCommands.hmget(randomKey, 'type', 'title', 'desc', 'imgUrl', 'numEp', 'numSeasons').each {
            map.put(it.key, it.value)
        }

        def type = map.get('type') ?: 'Unknown'
        def title = map.get('title') ?: 'Unknown Title'
        def desc = map.get('desc') ?: ''
        def img = map.get('imgUrl')
        def numEp = (map.get('numEp') as String).isInteger() ? map.get('numEp') as Integer : 1
        def numSeasons = (map.get('numSeasons') as String).isInteger() ? map.get('numSeasons') as Integer : 1

        return new SeriesRecommendation(type: type, title: title, description: desc, imageURL: img, numEpisodes: numEp, numSeasons: numSeasons)
    }
}
