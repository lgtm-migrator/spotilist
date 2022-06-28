package de.dargmuesli.spotilist.providers.spotify

import de.dargmuesli.spotilist.persistence.cache.SpotifyCache
import de.dargmuesli.spotilist.persistence.config.SpotifyConfig
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.requests.data.AbstractDataPagingRequest
import java.net.URI

object SpotifyUtil {
    val spotifyApiBuilder: SpotifyApi.Builder = SpotifyApi.builder()
        .setClientId(SpotifyConfig.clientId.value)
        .setClientSecret(SpotifyConfig.clientSecret.value)
        .setRedirectUri(URI(SpotifyConfig.redirectUri.value))
    var spotifyApi: SpotifyApi = spotifyApiBuilder.build()

    init {
        if (SpotifyCache.accessToken.value != "") {
            spotifyApi = spotifyApiBuilder
                .setAccessToken(SpotifyCache.accessToken.value)
                .build()
        }
    }

    fun openAuthorization() {
        val authorizationCode = spotifyApi.clientCredentials()
            .build().execute()

        SpotifyCache.accessToken.value = authorizationCode.accessToken
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