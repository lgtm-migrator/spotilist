package de.dargmuesli.spotilist.models.music

data class Playlist(
    val name: String = String(),
    val tracks: List<Track> = listOf()
) {
    override fun toString(): String {
        return name
    }
}