package de.dargmuesli.spotilist.providers

import de.dargmuesli.spotilist.providers.provider.*

/**
 * An enumeration of all possible module types.
 */
enum class SpotilistProviderType(val type: ISpotilistProvider) {
    NONE(NoneProvider),

    FILESYSTEM(FileSystemProvider),
    MP3TAG(Mp3TagProvider),
    SPOTIFY(SpotifyProvider);

    companion object {
        private val map = HashMap<ISpotilistProvider, SpotilistProviderType>()

        init {
            for (SpotilistProvider in values()) {
                map[SpotilistProvider.type] = SpotilistProvider
            }
        }

        fun isValid(provider: SpotilistProviderType): Boolean {
            return when (provider) {
                FILESYSTEM -> true
                MP3TAG -> true
                NONE -> true
                SPOTIFY -> SpotifyProvider.isAuthorized()
            }
        }

        fun keyOf(type: ISpotilistProvider): SpotilistProviderType? {
            return map[type]
        }
    }
}
