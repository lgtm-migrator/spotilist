package de.jonas_thelemann.dargmusic.models.enums

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
            for (DargmusicModuleType in values()) {
                map[DargmusicModuleType.type] = DargmusicModuleType
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
