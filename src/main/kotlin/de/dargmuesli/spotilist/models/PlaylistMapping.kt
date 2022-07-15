package de.dargmuesli.spotilist.models

import de.dargmuesli.spotilist.persistence.Persistence
import de.dargmuesli.spotilist.persistence.PersistenceTypes
import de.dargmuesli.spotilist.persistence.SpotilistConfig
import de.dargmuesli.spotilist.util.Util
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = PlaylistMapping.Serializer::class)
data class PlaylistMapping(
    val name: SimpleStringProperty = SimpleStringProperty(Util.getUnusedPlaylistMappingName(SpotilistConfig.playlistMappings)).also {
        it.addListener { _ ->
            Persistence.save(PersistenceTypes.CONFIG)
        }
    },
    var sourceResource: PlaylistMappingResource = PlaylistMappingResource(),
    var targetResource: PlaylistMappingResource = PlaylistMappingResource(),
    var blacklistSource: ArrayList<String> = arrayListOf(),
    var blacklistTarget: ArrayList<String> = arrayListOf(),
    val isEnabled: SimpleBooleanProperty = SimpleBooleanProperty().also {
        it.addListener { _ ->
            Persistence.save(PersistenceTypes.CONFIG)
        }
    }
) {

    object Serializer : KSerializer<PlaylistMapping> {
        override val descriptor: SerialDescriptor = PlaylistMappingSurrogate.serializer().descriptor

        override fun serialize(encoder: Encoder, value: PlaylistMapping) {
            encoder.encodeSerializableValue(
                PlaylistMappingSurrogate.serializer(),
                PlaylistMappingSurrogate(
                    value.name.value,
                    value.sourceResource,
                    value.targetResource,
                    value.blacklistSource,
                    value.blacklistTarget,
                    value.isEnabled.value
                )
            )
        }

        override fun deserialize(decoder: Decoder): PlaylistMapping {
            val playlistMapping = decoder.decodeSerializableValue(PlaylistMappingSurrogate.serializer())
            return PlaylistMapping(
                SimpleStringProperty(playlistMapping.name),
                playlistMapping.sourceResource,
                playlistMapping.targetResource,
                playlistMapping.blacklistSource,
                playlistMapping.blacklistTarget,
                SimpleBooleanProperty(playlistMapping.isEnabled)
            )
        }
    }

    @Serializable
    @SerialName("PlaylistMapping")
    private data class PlaylistMappingSurrogate(
        val name: String?,
        val sourceResource: PlaylistMappingResource,
        val targetResource: PlaylistMappingResource,
        val blacklistSource: ArrayList<String>,
        val blacklistTarget: ArrayList<String>,
        val isEnabled: Boolean
    )
}
