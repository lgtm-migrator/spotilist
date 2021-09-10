package de.dargmuesli.dargmusic.persistence.state.data.providers.spotify

import com.wrapper.spotify.model_objects.specification.Playlist
import com.wrapper.spotify.model_objects.specification.Track
import de.dargmuesli.dargmusic.persistence.state.data.providers.IProviderData

object SpotifyData : IProviderData<Playlist, Track> {
    override var playlistData = mutableMapOf<String, Playlist>()
    override var playlistItemData = mutableMapOf<String, Track>()

    var accessToken: String? = null
}