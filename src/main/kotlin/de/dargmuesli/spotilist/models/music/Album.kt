package de.dargmuesli.spotilist.models.music

import de.dargmuesli.spotilist.models.enums.AlbumType
import kotlinx.serialization.Serializable

@Serializable
data class Album(
    val albumType: AlbumType = AlbumType.ALBUM,
    val artists: List<Artist>? = null,
    val coverBase64: String? = null,
    val genres: List<String>? = null,
    val id: String? = null,
    val name: String? = null,
    val tracks: List<Track>? = null
) {
    override fun toString(): String {
        return name ?: super.toString()
    }
}