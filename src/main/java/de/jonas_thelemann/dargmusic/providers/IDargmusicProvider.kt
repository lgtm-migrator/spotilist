package de.jonas_thelemann.dargmusic.providers

interface IDargmusicProvider<PT, TT> {
    fun getPlaylistId(playlist: PT): String
    fun getPlaylistName(playlist: PT): String
    fun isPlaylistIdValid(playlistId: String): Boolean
}
