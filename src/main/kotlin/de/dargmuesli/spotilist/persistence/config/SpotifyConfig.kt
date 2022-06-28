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

@Serializable(with = SpotifyConfig.Serializer::class)
object SpotifyConfig {
    val clientId = SimpleStringProperty().also {
        it.addListener { _ ->
            Persistence.save(PersistenceTypes.CONFIG)
        }
    }
    val clientSecret = SimpleStringProperty().also {
        it.addListener { _ ->
            Persistence.save(PersistenceTypes.CONFIG)
        }
    }
    val redirectUri = SimpleStringProperty().also {
        it.addListener { _ ->
            Persistence.save(PersistenceTypes.CONFIG)
        }
    }

    object Serializer : KSerializer<SpotifyConfig> {
        override val descriptor: SerialDescriptor = SpotifyConfigSurrogate.serializer().descriptor

        override fun serialize(encoder: Encoder, value: SpotifyConfig) {
            encoder.encodeSerializableValue(
                SpotifyConfigSurrogate.serializer(),
                SpotifyConfigSurrogate(clientId.value, clientSecret.value, redirectUri.value)
            )
        }

        override fun deserialize(decoder: Decoder): SpotifyConfig {
            val spotifyConfig = decoder.decodeSerializableValue(SpotifyConfigSurrogate.serializer())
            clientId.set(spotifyConfig.clientId)
            clientSecret.set(spotifyConfig.clientSecret)
            redirectUri.set(spotifyConfig.redirectUri)
            return SpotifyConfig
        }
    }

    @Serializable
    @SerialName("SpotifyConfig")
    private data class SpotifyConfigSurrogate(val clientId: String?, val clientSecret: String?, val redirectUri: String?)
}
