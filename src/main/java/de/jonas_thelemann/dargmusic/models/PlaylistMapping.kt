package de.jonas_thelemann.dargmusic.models

import de.jonas_thelemann.dargmusic.models.enums.DargmusicProvider
import de.jonas_thelemann.dargmusic.persistence.state.DargmusicState
import de.jonas_thelemann.dargmusic.providers.FileSystemProvider
import de.jonas_thelemann.dargmusic.providers.SpotifyProvider
import de.jonas_thelemann.dargmusic.util.Util

data class PlaylistMapping(var name: String = Util.getUnusedPlaylistMappingName(DargmusicState.data.playlistMappings),
                           var sourceProvider: DargmusicProvider = DargmusicProvider.NONE,
                           var sourceId: String = String(),
                           var targetProvider: DargmusicProvider = DargmusicProvider.NONE,
                           var targetId: String = String(),
                           var blacklistSource: Array<String> = arrayOf(),
                           var blacklistTarget: Array<String> = arrayOf()) {

    fun validate(): Boolean {
        return name != ""
                && sourceProvider != DargmusicProvider.NONE
                && validateId(sourceId, sourceProvider)
                && targetProvider != DargmusicProvider.NONE
                && validateId(targetId, targetProvider)
    }

    private fun validateId(id: String, type: DargmusicProvider): Boolean {
        return when (type) {
            DargmusicProvider.FILESYSTEM -> FileSystemProvider.isPlaylistIdValid(id)
            DargmusicProvider.NONE -> true
            DargmusicProvider.SPOTIFY -> SpotifyProvider.isPlaylistIdValid(id)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlaylistMapping

        if (name != other.name) return false
        if (sourceProvider != other.sourceProvider) return false
        if (targetProvider != other.targetProvider) return false
        if (sourceId != other.sourceId) return false
        if (targetId != other.targetId) return false
        if (!blacklistSource.contentEquals(other.blacklistSource)) return false
        if (!blacklistTarget.contentEquals(other.blacklistTarget)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + sourceProvider.hashCode()
        result = 31 * result + targetProvider.hashCode()
        result = 31 * result + sourceId.hashCode()
        result = 31 * result + targetId.hashCode()
        result = 31 * result + blacklistSource.contentHashCode()
        result = 31 * result + blacklistTarget.contentHashCode()
        return result
    }
}
