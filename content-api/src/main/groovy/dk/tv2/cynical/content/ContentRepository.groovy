package dk.tv2.cynical.content

import io.lettuce.core.KeyScanCursor
import io.lettuce.core.ScanArgs
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

    List<VideoMetadata> fetchVideoMetadataByKey(String key) {
        final RedisCommands<String, String> syncCommands = redisConnection.sync()
        return getVideoMetadata(syncCommands.keys(key), syncCommands)
    }

    List<VideoMetadata> fetchVideoMetadataWithLimit(Integer limit) {
        int limitInt = limit ?: 10
        final RedisCommands<String, String> syncCommands = redisConnection.sync()
        KeyScanCursor<String> cursor = syncCommands.scan(ScanArgs.Builder.limit(limitInt).match("content:*"))
        return getVideoMetadata(cursor.keys.take(limitInt), syncCommands)
    }

    private List<VideoMetadata> getVideoMetadata(List<String> keys, RedisCommands<String, String> syncCommands) {
        def metadataResults = []

        keys.each {
            def key = it
            def map = [:]
            syncCommands.hmget(it, 'type', 'title', 'desc', 'imgUrl', 'numEp', 'numSeasons', 'genre', 'season', 'episode').each {
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
            def episode = map.get('episode') as Integer
            def season = map.get('season') as Integer
            def genre = map.get('genre')

            metadataResults << new VideoMetadata(
                    id: key,
                    type: type,
                    title: title,
                    description: desc,
                    imageURL: img,
                    numEpisodes: numEp,
                    numSeasons: numSeasons,
                    genre: genre,
                    episode: episode,
                    season: season
            )

        }
        return metadataResults
    }

}
