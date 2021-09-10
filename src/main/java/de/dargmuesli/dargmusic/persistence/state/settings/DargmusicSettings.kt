package de.dargmuesli.dargmusic.persistence.state.settings

import de.dargmuesli.dargmusic.persistence.state.settings.spotify.SpotifySettings
import de.dargmuesli.dargmusic.persistence.state.settings.youtube.YouTubeSettings

object DargmusicSettings {
    var spotifySettings: SpotifySettings = SpotifySettings
    var youTubeSettings: YouTubeSettings = YouTubeSettings
}
