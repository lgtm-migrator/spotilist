package de.dargmuesli.spotilist.persistence.state.settings.spotify

import java.net.URI

object SpotifySettings {
    var clientId = String()
    var clientSecret = String()
    var redirectUri = URI("")
}
