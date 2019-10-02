package de.jonas_thelemann.dargmusic.providers.spotify

import com.wrapper.spotify.SpotifyApi
import com.wrapper.spotify.requests.data.IPagingRequestBuilder
import de.jonas_thelemann.dargmusic.persistence.state.DargmusicState
import de.jonas_thelemann.dargmusic.persistence.state.data.providers.spotify.SpotifyData
import java.awt.Desktop
import java.time.Instant

object SpotifyUtil {
    val spotifyApiBuilder: SpotifyApi.Builder = SpotifyApi.builder()
            .setClientId(DargmusicState.settings.spotifySettings.clientId)
            .setClientSecret(DargmusicState.settings.spotifySettings.clientSecret)
            .setRedirectUri(DargmusicState.settings.spotifySettings.redirectUri)
    var spotifyApi: SpotifyApi = spotifyApiBuilder.build()

    init {
        if (DargmusicState.data.spotifyData.authorizationData.authorizationCodeCredentials.accessToken != "") {
            spotifyApi = spotifyApiBuilder
                    .setAccessToken(DargmusicState.data.spotifyData.authorizationData.authorizationCodeCredentials.accessToken)
                    .build()
        }
    }

    fun openAuthorization() {
        val uri = spotifyApi.authorizationCodeUri()
//                .scope("playlist-modify-private playlist-read-private user-read-private")
                .state("dargmusic")
                .build().execute()

        val os = System.getProperty("os.name").toLowerCase()
        val runtime = Runtime.getRuntime()

        SpotifyData.authorizationData.authorizationStarted = Instant.now().epochSecond

        if (os.contains("mac")) {
            runtime.exec("open $uri")
        } else if (os.contains("nix") || os.contains("nux")) {
            runtime.exec("xdg-open $uri")
        } else if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(uri)
        }
    }

    fun setAuthorizationCodeCredentials(authorizationCode: String) {
        if (authorizationCode == "") {
            return
        }

        DargmusicState.data.spotifyData.authorizationData.authorizationCodeCredentials = spotifyApi.authorizationCode(authorizationCode)
                .build().execute()
    }

    fun <T : Any?> getAllPagingItems(requestBuilder: IPagingRequestBuilder<T, *>): List<T> {
        val list = arrayListOf<T>()

        do {
            val paging = requestBuilder.build().execute()
            list.addAll(paging.items.asList())
            requestBuilder
                    .offset(paging.offset + paging.limit)
        } while (paging.next != null)

        return list
    }
}