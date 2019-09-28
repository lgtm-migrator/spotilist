package de.jonas_thelemann.dargmusic.persistence.state.data

import de.jonas_thelemann.dargmusic.models.PlaylistMapping
import de.jonas_thelemann.dargmusic.persistence.state.data.providers.spotify.SpotifyData

object DargmusicData {
    var playlistMappings: MutableList<PlaylistMapping> = mutableListOf()
    var spotifyData = SpotifyData
}
