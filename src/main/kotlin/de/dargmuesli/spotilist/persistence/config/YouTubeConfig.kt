package de.dargmuesli.spotilist.persistence.config

import de.dargmuesli.spotilist.persistence.Persistence
import de.dargmuesli.spotilist.persistence.PersistenceTypes
import javafx.beans.property.SimpleStringProperty
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = YouTubeConfig.Serializer::class)
object YouTubeConfig {
    val apiKey = SimpleStringProperty().also {
        it.addListener { _ ->
            Persistence.save(PersistenceTypes.CONFIG)
        }
    }

    object Serializer : KSerializer<YouTubeConfig> {
        override val descriptor: SerialDescriptor = YouTubeConfigSurrogate.serializer().descriptor

        override fun serialize(encoder: Encoder, value: YouTubeConfig) {
            encoder.encodeSerializableValue(
                YouTubeConfigSurrogate.serializer(),
                YouTubeConfigSurrogate(apiKey.value)
            )
        }

        override fun deserialize(decoder: Decoder): YouTubeConfig {
            val youTubeConfig = decoder.decodeSerializableValue(YouTubeConfigSurrogate.serializer())
            apiKey.set(youTubeConfig.apiKey)
            return YouTubeConfig
        }
    }

    @Serializable
    @SerialName("YouTubeConfig")
    private data class YouTubeConfigSurrogate(val apiKey: String?)
}
