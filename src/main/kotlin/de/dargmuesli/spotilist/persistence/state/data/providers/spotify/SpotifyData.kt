package de.dargmuesli.spotilist.persistence.state.data.providers.spotify

import de.dargmuesli.spotilist.persistence.state.data.providers.IProviderData
import se.michaelthelin.spotify.model_objects.specification.Playlist
import se.michaelthelin.spotify.model_objects.specification.Track

object SpotifyData : IProviderData<Playlist, Track> {
    override var playlistData = mutableMapOf<String, Playlist>()
    override var playlistItemData = mutableMapOf<String, Track>()

    var accessToken: String? = null
}