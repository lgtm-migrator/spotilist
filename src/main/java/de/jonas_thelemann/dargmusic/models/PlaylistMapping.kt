package de.jonas_thelemann.dargmusic.models

import de.jonas_thelemann.dargmusic.models.enums.DargmusicProviderType

data class PlaylistMapping<S, T> (private val name: String,
                                  private val sourceResourceType: DargmusicProviderType,
                                  private val targetResourceType: DargmusicProviderType,
                                  private val sourceId: String,
                                  private val targetId: String,
                                  private val blacklistSource: Array<S>,
                                  private val blacklistTarget: Array<T>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlaylistMapping<*, *>

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
