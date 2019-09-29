package de.jonas_thelemann.dargmusic.models.enums

import de.jonas_thelemann.dargmusic.models.PlaylistMappingResource
import de.jonas_thelemann.dargmusic.models.music.Playlist
import de.jonas_thelemann.dargmusic.providers.IDargmusicProvider
import de.jonas_thelemann.dargmusic.providers.NoneProvider
import de.jonas_thelemann.dargmusic.providers.filesystem.FileSystemProvider
import de.jonas_thelemann.dargmusic.providers.spotify.SpotifyProvider
import java.util.*

/**
 * An enumeration of all possible module types.
 */
enum class DargmusicProvider(val type: IDargmusicProvider) {
    NONE(NoneProvider),

    FILESYSTEM(FileSystemProvider),
    SPOTIFY(SpotifyProvider);

    companion object {
        private val map = HashMap<IDargmusicProvider, DargmusicProvider>()

        init {
            for (DargmusicProvider in values()) {
                map[DargmusicProvider.type] = DargmusicProvider
            }
        }

        fun getPlaylist(pmResource: PlaylistMappingResource): Playlist {
            return when (pmResource.provider) {
                FILESYSTEM -> FileSystemProvider.getPlaylist(pmResource.id)
                NONE -> NoneProvider.getPlaylist(String())
                SPOTIFY -> SpotifyProvider.getPlaylist(pmResource.id)
            }
        }

        fun isValid(provider: DargmusicProvider): Boolean {
            return when (provider) {
                FILESYSTEM -> true
                NONE -> true
                SPOTIFY -> SpotifyProvider.isAuthorized()
            }
        }

        fun isValid(pmResource: PlaylistMappingResource): Boolean {
            return when (pmResource.provider) {
                FILESYSTEM -> FileSystemProvider.isPlaylistIdValid(pmResource.id)
                NONE -> NoneProvider.isPlaylistIdValid(pmResource.id)
                SPOTIFY -> SpotifyProvider.isAuthorized() && SpotifyProvider.isPlaylistIdValid(pmResource.id)
            }
        }

        fun keyOf(type: IDargmusicProvider): DargmusicProvider? {
            return map[type]
        }
    }
}
