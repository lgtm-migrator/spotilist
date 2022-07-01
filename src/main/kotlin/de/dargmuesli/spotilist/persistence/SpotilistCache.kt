package de.dargmuesli.spotilist.persistence

import de.dargmuesli.spotilist.persistence.cache.SpotifyCache
import de.dargmuesli.spotilist.persistence.cache.YouTubeCache
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


@Serializable(with = SpotilistCache.Serializer::class)
object SpotilistCache : AbstractSerializable() {
    var spotify = SpotifyCache
    var youTube = YouTubeCache

    object Serializer : KSerializer<SpotilistCache> {
        override val descriptor: SerialDescriptor = SpotilistCacheSurrogate.serializer().descriptor

        override fun serialize(encoder: Encoder, value: SpotilistCache) {
            encoder.encodeSerializableValue(
                SpotilistCacheSurrogate.serializer(),
                SpotilistCacheSurrogate(spotify, youTube)
            )
        }

        override fun deserialize(decoder: Decoder): SpotilistCache {
            val spotilistCache = decoder.decodeSerializableValue(SpotilistCacheSurrogate.serializer())
            spotify = spotilistCache.spotify
            youTube = spotilistCache.youTube
            return SpotilistCache
        }
    }

    @Serializable
    @SerialName("SpotilistCache")
    private data class SpotilistCacheSurrogate(
        val spotify: SpotifyCache,
        val youTube: YouTubeCache
    )
}
