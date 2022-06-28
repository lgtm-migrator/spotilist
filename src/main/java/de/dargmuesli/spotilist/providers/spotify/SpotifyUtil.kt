package de.dargmuesli.spotilist.providers.spotify

import de.dargmuesli.spotilist.persistence.state.SpotilistState
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.requests.data.AbstractDataPagingRequest

object SpotifyUtil {
    val spotifyApiBuilder: SpotifyApi.Builder = SpotifyApi.builder()
        .setClientId(SpotilistState.settings.spotifySettings.clientId)
        .setClientSecret(SpotilistState.settings.spotifySettings.clientSecret)
        .setRedirectUri(SpotilistState.settings.spotifySettings.redirectUri)
    var spotifyApi: SpotifyApi = spotifyApiBuilder.build()

    init {
        if (SpotilistState.data.spotifyData.accessToken != "") {
            spotifyApi = spotifyApiBuilder
                .setAccessToken(SpotilistState.data.spotifyData.accessToken)
                .build()
        }
    }

    fun openAuthorization() {
        val authorizationCode = spotifyApi.clientCredentials()
            .build().execute()

        SpotilistState.data.spotifyData.accessToken = authorizationCode.accessToken
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