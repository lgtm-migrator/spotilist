package de.dargmuesli.spotilist.models.music

import kotlinx.serialization.Serializable

@Serializable
data class Playlist(
    val id: String? = null,
    val name: String? = null,
    val tracks: List<Track>? = null
) {
    override fun toString(): String {
        return name ?: super.toString()
    }
}