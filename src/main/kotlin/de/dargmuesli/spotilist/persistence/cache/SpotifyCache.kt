package de.dargmuesli.spotilist.persistence.cache

import de.dargmuesli.spotilist.persistence.Persistence
import de.dargmuesli.spotilist.persistence.PersistenceTypes
import de.dargmuesli.spotilist.util.serializer.SpotifyPlaylistSerializer
import de.dargmuesli.spotilist.util.serializer.SpotifyTrackSerializer
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections.observableHashMap
import javafx.collections.ObservableMap
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import se.michaelthelin.spotify.model_objects.specification.Playlist
import se.michaelthelin.spotify.model_objects.specification.Track


@Serializable(with = SpotifyCache.Serializer::class)
object SpotifyCache : IProviderCache<Playlist, Track> {
    override var playlistData: ObservableMap<String, Playlist> = observableHashMap()
    override var playlistItemData: ObservableMap<String, Track> = observableHashMap()

    val accessToken = SimpleStringProperty().also {
        it.addListener { _, _, _ -> Persistence.save(PersistenceTypes.CACHE) }
    }

    object Serializer : KSerializer<SpotifyCache> {
        override val descriptor: SerialDescriptor = SpotifyCacheSurrogate.serializer().descriptor

        override fun serialize(encoder: Encoder, value: SpotifyCache) {
            encoder.encodeSerializableValue(
                SpotifyCacheSurrogate.serializer(),
                SpotifyCacheSurrogate(playlistData.toMap(), playlistItemData.toMap())
            )
        }

        override fun deserialize(decoder: Decoder): SpotifyCache {
            val spotifyCache = decoder.decodeSerializableValue(SpotifyCacheSurrogate.serializer())
            playlistData.putAll(spotifyCache.playlistData)
            playlistItemData.putAll(spotifyCache.playlistItemData)
            return SpotifyCache
        }
    }

    @Serializable
    @SerialName("SpotifyCache")
    private data class SpotifyCacheSurrogate(
        val playlistData: Map<String, @Serializable(with = SpotifyPlaylistSerializer.Serializer::class) Playlist>,
        val playlistItemData: Map<String, @Serializable(with = SpotifyTrackSerializer.Serializer::class) Track>
    )

    override fun clear() {
        for (entry in playlistData) {
            playlistData.remove(entry.key)
        }

        for (entry in playlistItemData) {
            playlistItemData.remove(entry.key)
        }
    }
}