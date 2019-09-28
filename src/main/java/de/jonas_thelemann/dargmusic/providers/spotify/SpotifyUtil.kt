package de.jonas_thelemann.dargmusic.providers.spotify

import com.wrapper.spotify.requests.data.IPagingRequestBuilder
import de.jonas_thelemann.dargmusic.persistence.state.DargmusicState
import de.jonas_thelemann.dargmusic.persistence.state.data.providers.spotify.SpotifyData
import java.awt.Desktop
import java.time.Instant

object SpotifyUtil {

    fun openAuthorization() {
        val uri = SpotifyProvider.spotifyApi.authorizationCodeUri()
                .redirect_uri(DargmusicState.settings.spotifySettings.redirectUri)
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

        DargmusicState.data.spotifyData.authorizationData.authorizationCodeCredentials = SpotifyProvider.spotifyApi.authorizationCode(authorizationCode)
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