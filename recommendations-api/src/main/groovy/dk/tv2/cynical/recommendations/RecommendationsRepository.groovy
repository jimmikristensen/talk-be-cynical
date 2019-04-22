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

    List<SeriesRecommendation> fetchRecommendation(Integer limit) {
        int limitInt = limit ?: 10
        def recommendationResult = []

        final RedisCommands<String, String> syncCommands = redisConnection.sync()
        def dbSize = syncCommands.dbsize()

        getRandomIndices(dbSize, limitInt).each {
            def map = [:]
            def key = it as String

            syncCommands.hmget(key, 'type', 'title', 'desc', 'imgUrl', 'numEp', 'numSeasons').each {
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
            def numEp = map.get('numEp') as Integer
            def numSeasons = map.get('numSeasons') as Integer

            recommendationResult << new SeriesRecommendation(
                    id: key,
                    type: type,
                    title: title,
                    description: desc,
                    imageURL: img,
                    numEpisodes: numEp,
                    numSeasons: numSeasons
            )
        }

        return recommendationResult
    }

    private ArrayList<String> getRandomIndices(long dbSize, int count) {
        count = 0 < count && count <= dbSize ? count : 10 // make sure we cant get a count greather than dbsize and less than 1

        ArrayList indices = new ArrayList()
        (1..dbSize).each {
            indices << "rec:series${it}"
        }
        Collections.shuffle(indices)
        return indices.take(count)
    }
}
