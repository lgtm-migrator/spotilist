package de.jonas_thelemann.dargmusic.persistence.settings

import de.jonas_thelemann.dargmusic.persistence.settings.spotify.SpotifySettings
import de.jonas_thelemann.dargmusic.persistence.settings.youtube.YouTubeSettings

object DargmusicSettings {
    var spotifySettings: SpotifySettings = SpotifySettings
    var youTubeSettings: YouTubeSettings = YouTubeSettings
}
