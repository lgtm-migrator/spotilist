package de.dargmuesli.spotilist.models

import de.dargmuesli.spotilist.providers.SpotilistProviderType
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = PlaylistMappingResource.Serializer::class)
data class PlaylistMappingResource(
    var provider: SimpleStringProperty = SimpleStringProperty(SpotilistProviderType.NONE.name),
    var id: SimpleStringProperty = SimpleStringProperty(),
    var isValid: SimpleBooleanProperty = SimpleBooleanProperty()
) {
    init {
        provider.addListener { _ ->
            updateValidity()
        }

        id.addListener { _ ->
            updateValidity()
        }

        updateValidity()
    }

    private fun updateValidity() {
        val provider = SpotilistProviderType.valueOf(provider.value)
        isValid.set(SpotilistProviderType.isValid(provider) && provider.type.isPlaylistIdValid(id.value))
    }

    object Serializer : KSerializer<PlaylistMappingResource> {
        override val descriptor: SerialDescriptor = PlaylistMappingResourceSurrogate.serializer().descriptor

        override fun serialize(encoder: Encoder, value: PlaylistMappingResource) {
            encoder.encodeSerializableValue(
                PlaylistMappingResourceSurrogate.serializer(),
                PlaylistMappingResourceSurrogate(
                    value.provider.value,
                    value.id.value
                )
            )
        }

        override fun deserialize(decoder: Decoder): PlaylistMappingResource {
            val surrogate = decoder.decodeSerializableValue(PlaylistMappingResourceSurrogate.serializer())
            return PlaylistMappingResource(
                SimpleStringProperty(surrogate.provider),
                SimpleStringProperty(surrogate.id)
            )
        }
    }

    @Serializable
    @SerialName("PlaylistMappingResource")
    private data class PlaylistMappingResourceSurrogate(
        val provider: String,
        val id: String
    )
}