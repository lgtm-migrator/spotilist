package de.jonas_thelemann.dargmusic.models.enums

import java.util.HashMap

/**
 * An enumeration of all possible module types.
 */
enum class DargmusicProviderType(val type: String) {
    YOUTUBE("youtube"),
    FILESYSTEM("filesystem");

    companion object {
        private val map = HashMap<String, DargmusicProviderType>()

        init {
            for (DargmusicModuleType in values()) {
                map[DargmusicModuleType.type] = DargmusicModuleType
            }
        }

        fun keyOf(type: String): DargmusicProviderType? {
            return map[type]
        }
    }
}
