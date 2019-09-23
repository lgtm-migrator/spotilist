package de.jonas_thelemann.dargmusic.persistence.state.settings

import de.jonas_thelemann.dargmusic.persistence.state.settings.spotify.SpotifySettings
import de.jonas_thelemann.dargmusic.persistence.state.settings.youtube.YouTubeSettings

object DargmusicSettings {
    var spotifySettings: SpotifySettings = SpotifySettings
    var youTubeSettings: YouTubeSettings = YouTubeSettings
}
