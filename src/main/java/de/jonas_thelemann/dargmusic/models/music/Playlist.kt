package de.jonas_thelemann.dargmusic.models.music

data class Playlist(val name: String = String(),
                    val tracks: List<Track> = listOf())