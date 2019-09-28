package de.jonas_thelemann.dargmusic.persistence.state.data.providers.spotify

import com.wrapper.spotify.model_objects.specification.Playlist
import com.wrapper.spotify.model_objects.specification.Track
import de.jonas_thelemann.dargmusic.persistence.state.data.providers.IProviderData
import de.jonas_thelemann.dargmusic.persistence.state.data.providers.spotify.authorization.SpotifyAuthorizationData

object SpotifyData : IProviderData<Playlist, Track> {
    override var playlistData = mutableMapOf<String, Playlist>()
    override var playlistItemData = mutableMapOf<String, Track>()

    var authorizationData = SpotifyAuthorizationData
}