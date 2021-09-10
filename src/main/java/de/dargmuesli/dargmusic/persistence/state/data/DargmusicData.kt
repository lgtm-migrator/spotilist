package de.dargmuesli.dargmusic.persistence.state.data

import de.dargmuesli.dargmusic.models.PlaylistMapping
import de.dargmuesli.dargmusic.persistence.state.data.providers.spotify.SpotifyData

object DargmusicData {
    var playlistMappings: MutableList<PlaylistMapping> = mutableListOf()
    var spotifyData = SpotifyData
}
