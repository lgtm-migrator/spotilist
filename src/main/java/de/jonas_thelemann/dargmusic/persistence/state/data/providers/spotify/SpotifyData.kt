package de.jonas_thelemann.dargmusic.persistence.state.data.providers.spotify

import de.jonas_thelemann.dargmusic.providers.SpotifyProvider
import kotlin.properties.Delegates

object SpotifyData {
    var accessToken by Delegates.observable(String()) {
        _, _, newValue -> SpotifyProvider.spotifyApi = SpotifyProvider.spotifyApiBuilder
            .setAccessToken(newValue)
            .build()
    }
    var refreshToken by Delegates.observable(String()) {
        _, _, newValue -> SpotifyProvider.spotifyApi = SpotifyProvider.spotifyApiBuilder
            .setRefreshToken(newValue)
            .build()
    }
}