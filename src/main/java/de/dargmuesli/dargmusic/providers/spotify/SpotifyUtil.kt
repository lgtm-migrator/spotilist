package de.dargmuesli.dargmusic.providers.spotify

import de.dargmuesli.dargmusic.persistence.state.DargmusicState
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.requests.data.AbstractDataPagingRequest

object SpotifyUtil {
    val spotifyApiBuilder: SpotifyApi.Builder = SpotifyApi.builder()
        .setClientId(DargmusicState.settings.spotifySettings.clientId)
        .setClientSecret(DargmusicState.settings.spotifySettings.clientSecret)
        .setRedirectUri(DargmusicState.settings.spotifySettings.redirectUri)
    var spotifyApi: SpotifyApi = spotifyApiBuilder.build()

    init {
        if (DargmusicState.data.spotifyData.accessToken != "") {
            spotifyApi = spotifyApiBuilder
                .setAccessToken(DargmusicState.data.spotifyData.accessToken)
                .build()
        }
    }

    fun openAuthorization() {
        val authorizationCode = spotifyApi.clientCredentials()
            .build().execute()

        DargmusicState.data.spotifyData.accessToken = authorizationCode.accessToken
    }

    fun <T> getAllPagingItems(requestBuilder: AbstractDataPagingRequest.Builder<T, *>): List<T> {
        val list = arrayListOf<T>()

        do {
            val paging = requestBuilder.build().execute()
            list.addAll(paging.items)
            requestBuilder
                .offset(paging.offset + paging.limit)
        } while (paging.next != null)

        return list
    }
}