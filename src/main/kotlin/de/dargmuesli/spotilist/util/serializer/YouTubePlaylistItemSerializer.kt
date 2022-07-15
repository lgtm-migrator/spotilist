package de.dargmuesli.spotilist.util.serializer

import com.google.api.services.youtube.model.PlaylistItem
import com.google.gson.Gson
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class YouTubePlaylistItemSerializer {
    object Serializer : KSerializer<PlaylistItem> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("PlaylistItem", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: PlaylistItem) {
            encoder.encodeString(
                Gson().toJson(value)
            )
        }

        override fun deserialize(decoder: Decoder): PlaylistItem {
//            YOUTUBE.jsonFactory.createJsonObjectParser().parseAndClose(Reader.nullReader(), PlaylistItem::class.java)
            return Gson().fromJson(decoder.decodeString(), PlaylistItem::class.java)
        }
    }
}