package de.dargmuesli.spotilist.models.music

import de.dargmuesli.spotilist.models.enums.AlbumType

data class Album(
    val albumType: AlbumType = AlbumType.ALBUM,
    val artists: List<Artist> = listOf(),
    val genres: List<String> = listOf(),
    val name: String = String(),
    val tracks: List<Track> = listOf()
) {
    override fun toString(): String {
        return name
    }
}