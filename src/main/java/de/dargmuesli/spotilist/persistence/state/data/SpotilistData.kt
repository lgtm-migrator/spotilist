package de.dargmuesli.spotilist.persistence.state.data

import de.dargmuesli.spotilist.models.PlaylistMapping
import de.dargmuesli.spotilist.persistence.state.data.providers.spotify.SpotifyData

object SpotilistData {
    var playlistMappings: MutableList<PlaylistMapping> = mutableListOf()
    var spotifyData = SpotifyData
}
