package de.jonas_thelemann.dargmusic.models.music

data class Track(val album: Album = Album(),
                 val artists: List<Artist> = listOf(),
                 val durationMs: Int = -1,
                 val name: String = String())