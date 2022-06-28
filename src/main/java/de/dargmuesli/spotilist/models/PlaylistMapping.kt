package de.dargmuesli.spotilist.models

import de.dargmuesli.spotilist.models.enums.SpotilistProvider
import de.dargmuesli.spotilist.persistence.state.SpotilistState
import de.dargmuesli.spotilist.util.Util

data class PlaylistMapping(
    var name: String = Util.getUnusedPlaylistMappingName(SpotilistState.data.playlistMappings),
    var sourceResource: PlaylistMappingResource = PlaylistMappingResource(),
    var targetResource: PlaylistMappingResource = PlaylistMappingResource(),
    var blacklistSource: Array<String> = arrayOf(),
    var blacklistTarget: Array<String> = arrayOf()
) {

    fun validate(): Boolean {
        return name != ""
                && sourceResource.provider != SpotilistProvider.NONE
                && SpotilistProvider.isPlaylistMappingValid(sourceResource)
                && targetResource.provider != SpotilistProvider.NONE
                && SpotilistProvider.isPlaylistMappingValid(targetResource)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlaylistMapping

        if (name != other.name) return false
        if (sourceResource != other.sourceResource) return false
        if (targetResource != other.targetResource) return false
        if (!blacklistSource.contentEquals(other.blacklistSource)) return false
        if (!blacklistTarget.contentEquals(other.blacklistTarget)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + sourceResource.hashCode()
        result = 31 * result + targetResource.hashCode()
        result = 31 * result + blacklistSource.contentHashCode()
        result = 31 * result + blacklistTarget.contentHashCode()
        return result
    }
}
