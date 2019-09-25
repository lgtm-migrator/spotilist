package de.jonas_thelemann.dargmusic.models

import de.jonas_thelemann.dargmusic.models.enums.DargmusicProviderType
import de.jonas_thelemann.dargmusic.persistence.state.DargmusicState
import de.jonas_thelemann.dargmusic.providers.FileSystemProvider
import de.jonas_thelemann.dargmusic.providers.SpotifyProvider
import de.jonas_thelemann.dargmusic.util.Util

data class PlaylistMapping(var name: String = Util.getUnusedSubscriptionName(DargmusicState.data.playlistMappings),
                           var sourceResourceType: DargmusicProviderType = DargmusicProviderType.NONE,
                           var targetResourceType: DargmusicProviderType = DargmusicProviderType.NONE,
                           var sourceId: String = String(),
                           var targetId: String = String(),
                           var blacklistSource: Array<String> = arrayOf(),
                           var blacklistTarget: Array<String> = arrayOf()) {

    fun validate(): Boolean {
        return name != ""
                && sourceResourceType != DargmusicProviderType.NONE
                && validateId(sourceId, sourceResourceType)
                && targetResourceType != DargmusicProviderType.NONE
                && validateId(targetId, targetResourceType)
    }

    private fun validateId(id: String, type: DargmusicProviderType): Boolean {
        return when (type) {
            DargmusicProviderType.FILESYSTEM -> FileSystemProvider.isPlaylistIdValid(id)
            DargmusicProviderType.NONE -> true
            DargmusicProviderType.SPOTIFY -> SpotifyProvider.isPlaylistIdValid(id)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlaylistMapping

        if (name != other.name) return false
        if (sourceResourceType != other.sourceResourceType) return false
        if (targetResourceType != other.targetResourceType) return false
        if (sourceId != other.sourceId) return false
        if (targetId != other.targetId) return false
        if (!blacklistSource.contentEquals(other.blacklistSource)) return false
        if (!blacklistTarget.contentEquals(other.blacklistTarget)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + sourceResourceType.hashCode()
        result = 31 * result + targetResourceType.hashCode()
        result = 31 * result + sourceId.hashCode()
        result = 31 * result + targetId.hashCode()
        result = 31 * result + blacklistSource.contentHashCode()
        result = 31 * result + blacklistTarget.contentHashCode()
        return result
    }
}
