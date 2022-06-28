package de.dargmuesli.spotilist.persistence

import de.dargmuesli.spotilist.persistence.config.SpotifyConfig
import de.dargmuesli.spotilist.persistence.config.YouTubeConfig
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = SpotilistConfig.Serializer::class)
object SpotilistConfig : AbstractSerializable() {
    var spotify: SpotifyConfig = SpotifyConfig
    var youTube: YouTubeConfig = YouTubeConfig

    object Serializer : KSerializer<SpotilistConfig> {
        override val descriptor: SerialDescriptor = SpotilistConfigSurrogate.serializer().descriptor

        override fun serialize(encoder: Encoder, value: SpotilistConfig) {
            encoder.encodeSerializableValue(
                SpotilistConfigSurrogate.serializer(),
                SpotilistConfigSurrogate(
                    spotify,
                    youTube
                )
            )
        }

        override fun deserialize(decoder: Decoder): SpotilistConfig {
            val spotilistConfig = decoder.decodeSerializableValue(SpotilistConfigSurrogate.serializer())
            spotify = spotilistConfig.spotify
            youTube = spotilistConfig.youTube
            return SpotilistConfig
        }
    }

    @Serializable
    @SerialName("SpotilistConfig")
    private data class SpotilistConfigSurrogate(
        val spotify: SpotifyConfig,
        val youTube: YouTubeConfig
    )
}
