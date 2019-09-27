package de.jonas_thelemann.dargmusic.models.enums

import com.wrapper.spotify.SpotifyApi
import de.jonas_thelemann.dargmusic.persistence.state.DargmusicState
import de.jonas_thelemann.dargmusic.providers.FileSystemProvider
import de.jonas_thelemann.dargmusic.providers.SpotifyProvider
import java.util.*

/**
 * An enumeration of all possible module types.
 */
enum class DargmusicProvider(val type: String) {
    NONE("none"),

    FILESYSTEM("Filesystem"),
    SPOTIFY("Spotify");

    companion object {
        private val map = HashMap<String, DargmusicProvider>()

        init {
            for (DargmusicProvider in values()) {
                map[DargmusicProvider.type] = DargmusicProvider
            }
        }

        fun isValid(provider: DargmusicProvider): Boolean {
            return when (provider) {
                NONE -> true
                FILESYSTEM -> true
                SPOTIFY -> SpotifyProvider.isValid()
            }
        }

        fun isIdValid(id: String, type: DargmusicProvider): Boolean {
            return when (type) {
                FILESYSTEM -> FileSystemProvider.isPlaylistIdValid(id)
                NONE -> true
                SPOTIFY -> SpotifyProvider.isValid() && SpotifyProvider.isPlaylistIdValid(id)
            }
        }

        fun keyOf(type: String): DargmusicProvider? {
            return map[type]
        }
    }

    override fun toString(): String {
        return this.type
    }
}
