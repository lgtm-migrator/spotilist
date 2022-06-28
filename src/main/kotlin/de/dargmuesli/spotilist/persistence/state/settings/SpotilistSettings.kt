package de.dargmuesli.spotilist.persistence.state.settings

import de.dargmuesli.spotilist.persistence.state.settings.spotify.SpotifySettings
import de.dargmuesli.spotilist.persistence.state.settings.youtube.YouTubeSettings

object SpotilistSettings {
    var spotifySettings: SpotifySettings = SpotifySettings
    var youTubeSettings: YouTubeSettings = YouTubeSettings
}
