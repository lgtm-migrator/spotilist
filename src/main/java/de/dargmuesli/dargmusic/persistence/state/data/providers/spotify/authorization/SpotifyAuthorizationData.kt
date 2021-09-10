package de.dargmuesli.dargmusic.persistence.state.data.providers.spotify.authorization

import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials
import de.dargmuesli.dargmusic.providers.spotify.SpotifyUtil
import java.time.Instant
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

object SpotifyAuthorizationData {
    private val refreshScheduler = Executors.newScheduledThreadPool(1)

    var authorizationCodeCredentials: AuthorizationCodeCredentials by Delegates.observable(AuthorizationCodeCredentials.Builder().build()) { _, _, newValue ->
        if (newValue.accessToken != null
                && newValue.expiresIn != null) {

            SpotifyUtil.spotifyApi = SpotifyUtil.spotifyApiBuilder
                    .setAccessToken(newValue.accessToken)
                    .build()

            if (newValue.refreshToken != null) {
                refreshTokenCopy = newValue.refreshToken
            }

            if (authorizationStarted != 0L) {
                scheduleAuthorizationRefresh(authorizationStarted, newValue)
            }
        }
    }

    var authorizationStarted: Long by Delegates.observable(0L) { _, _, newValue ->
        if (SpotifyUtil.spotifyApi.refreshToken != null) {
            scheduleAuthorizationRefresh(newValue, authorizationCodeCredentials)
        }
    }

    private var refreshTokenCopy by Delegates.observable("") { _, _, newValue ->
        SpotifyUtil.spotifyApi = SpotifyUtil.spotifyApiBuilder
                .setRefreshToken(newValue)
                .build()
    }

    private lateinit var scheduleFuture: ScheduledFuture<*>

    private fun scheduleAuthorizationRefresh(authorizationStartedNew: Long, authorizationCodeCredentialsNew: AuthorizationCodeCredentials) {
        val secondsRemaining = Instant.ofEpochSecond(authorizationStartedNew).plusSeconds(authorizationCodeCredentialsNew.expiresIn.toLong()).minusSeconds(Instant.now().epochSecond).epochSecond

        if (::scheduleFuture.isInitialized) {
            scheduleFuture.cancel(false)
        }

        scheduleFuture = refreshScheduler.schedule({
            authorizationStarted = Instant.now().epochSecond
            authorizationCodeCredentials = SpotifyUtil.spotifyApi.authorizationCodeRefresh().build().execute()
        }, secondsRemaining, TimeUnit.SECONDS)
    }
}