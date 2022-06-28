package de.dargmuesli.spotilist.persistence

import kotlinx.serialization.Serializable

enum class PersistenceTypes(@Serializable val spotilistPersistence: AbstractSerializable) {
    CACHE(SpotilistCache),
    CONFIG(SpotilistConfig);

    companion object {
        private val map: MutableMap<AbstractSerializable, PersistenceTypes> = HashMap()

        init {
            for (persistenceType in PersistenceTypes.values()) {
                map[persistenceType.spotilistPersistence] = persistenceType
            }
        }

        fun keyOf(type: AbstractSerializable): PersistenceTypes? {
            return map[type]
        }
    }
}