package de.dargmuesli.spotilist.util.serializer

import com.google.api.services.youtube.model.Playlist
import com.google.gson.Gson
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class YouTubePlaylistSerializer {
    object Serializer : KSerializer<Playlist> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Playlist", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: Playlist) {
            encoder.encodeString(
                Gson().toJson(value)
            )
        }

        override fun deserialize(decoder: Decoder): Playlist {
            return Gson().fromJson(decoder.decodeString(), Playlist::class.java)
        }
    }
}