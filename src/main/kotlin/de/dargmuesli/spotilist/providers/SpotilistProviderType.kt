package de.dargmuesli.spotilist.providers

import de.dargmuesli.spotilist.providers.provider.FileSystemProvider
import de.dargmuesli.spotilist.providers.provider.NoneProvider
import de.dargmuesli.spotilist.providers.provider.SpotifyProvider
import de.dargmuesli.spotilist.providers.provider.YouTubeProvider

/**
 * An enumeration of all possible module types.
 */
enum class SpotilistProviderType(val type: ISpotilistProvider<*, *>) {
    NONE(NoneProvider),

    FILESYSTEM(FileSystemProvider),
    SPOTIFY(SpotifyProvider),
    YOUTUBE(YouTubeProvider);

    companion object {
        private val map = HashMap<ISpotilistProvider<*, *>, SpotilistProviderType>()

        init {
            for (SpotilistProvider in values()) {
                map[SpotilistProvider.type] = SpotilistProvider
            }
        }

        fun isValid(provider: SpotilistProviderType): Boolean {
            return when (provider) {
                FILESYSTEM -> true
                NONE -> false
                SPOTIFY -> SpotifyProvider.isAuthorized()
                YOUTUBE -> YouTubeProvider.isAuthorized()
            }
        }

        fun keyOf(type: ISpotilistProvider<*, *>): SpotilistProviderType? {
            return map[type]
        }
    }
}
