package de.dargmuesli.spotilist.models.enums

import de.dargmuesli.spotilist.models.PlaylistMappingResource
import de.dargmuesli.spotilist.models.music.Playlist
import de.dargmuesli.spotilist.providers.ISpotilistProvider
import de.dargmuesli.spotilist.providers.NoneProvider
import de.dargmuesli.spotilist.providers.file_system.FileSystemProvider
import de.dargmuesli.spotilist.providers.mp3tag.Mp3TagProvider
import de.dargmuesli.spotilist.providers.spotify.SpotifyProvider

/**
 * An enumeration of all possible module types.
 */
enum class SpotilistProvider(val type: ISpotilistProvider) {
    NONE(NoneProvider),

    FILESYSTEM(FileSystemProvider),
    MP3TAG(Mp3TagProvider),
    SPOTIFY(SpotifyProvider);

    companion object {
        private val map = HashMap<ISpotilistProvider, SpotilistProvider>()

        init {
            for (SpotilistProvider in values()) {
                map[SpotilistProvider.type] = SpotilistProvider
            }
        }

        fun getPlaylist(pmResource: PlaylistMappingResource): Playlist {
            return when (pmResource.provider) {
                FILESYSTEM -> FileSystemProvider.getPlaylist(pmResource.id)
                MP3TAG -> Mp3TagProvider.getPlaylist(pmResource.id)
                NONE -> NoneProvider.getPlaylist(String())
                SPOTIFY -> SpotifyProvider.getPlaylist(pmResource.id)
            }
        }

        fun isValid(provider: SpotilistProvider): Boolean {
            return when (provider) {
                FILESYSTEM -> true
                MP3TAG -> true
                NONE -> true
                SPOTIFY -> SpotifyProvider.isAuthorized()
            }
        }

        fun isPlaylistMappingValid(pmResource: PlaylistMappingResource): Boolean {
            return when (pmResource.provider) {
                FILESYSTEM -> FileSystemProvider.isPlaylistIdValid(pmResource.id)
                MP3TAG -> Mp3TagProvider.isPlaylistIdValid(pmResource.id)
                NONE -> NoneProvider.isPlaylistIdValid(pmResource.id)
                SPOTIFY -> SpotifyProvider.isAuthorized() && SpotifyProvider.isPlaylistIdValid(pmResource.id)
            }
        }

        fun keyOf(type: ISpotilistProvider): SpotilistProvider? {
            return map[type]
        }
    }
}
