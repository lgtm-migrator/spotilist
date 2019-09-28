package de.jonas_thelemann.dargmusic.persistence.state.data.providers.spotify.authorization

import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials
import de.jonas_thelemann.dargmusic.providers.spotify.SpotifyProvider
import java.time.Instant
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

object SpotifyAuthorizationData {
    private val refreshScheduler = Executors.newScheduledThreadPool(1)

    var authorizationCodeCredentials: AuthorizationCodeCredentials by Delegates.observable(AuthorizationCodeCredentials.Builder().build()) { _, _, newValue ->
        if (newValue.accessToken != null
                && newValue.expiresIn != null) {

            SpotifyProvider.spotifyApi = SpotifyProvider.spotifyApiBuilder
                    .setAccessToken(newValue.accessToken)
                    .build()

            if (newValue.refreshToken != null) {
                refreshTokenCopy = newValue.refreshToken
            }

            if (authorizationStarted != 0L) {
                val secondsRemaining = Instant.ofEpochSecond(authorizationStarted).plusSeconds(newValue.expiresIn.toLong()).minusSeconds(Instant.now().epochSecond).epochSecond
                refreshScheduler.schedule({
                    authorizationStarted = Instant.now().epochSecond
                    authorizationCodeCredentials = SpotifyProvider.spotifyApi.authorizationCodeRefresh().build().execute()
                }, secondsRemaining, TimeUnit.SECONDS)
            }
        }
    }
    var authorizationStarted: Long by Delegates.observable(0L) { _, _, newValue ->
        if (SpotifyProvider.spotifyApi.refreshToken != null) {
            val secondsRemaining = Instant.ofEpochSecond(newValue).plusSeconds(authorizationCodeCredentials.expiresIn.toLong()).minusSeconds(Instant.now().epochSecond).epochSecond
            refreshScheduler.schedule({
                authorizationStarted = Instant.now().epochSecond
                authorizationCodeCredentials = SpotifyProvider.spotifyApi.authorizationCodeRefresh().build().execute()
            }, secondsRemaining, TimeUnit.SECONDS)
        }
    }
    private var refreshTokenCopy by Delegates.observable("") { _, _, newValue ->
        SpotifyProvider.spotifyApi = SpotifyProvider.spotifyApiBuilder
                .setRefreshToken(newValue)
                .build()
    }
}