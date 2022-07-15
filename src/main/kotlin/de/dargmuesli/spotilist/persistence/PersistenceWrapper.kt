package de.dargmuesli.spotilist.persistence

import kotlinx.serialization.Serializable

@Serializable
object PersistenceWrapper {
    var cache = SpotilistCache
    var config = SpotilistConfig

    operator fun get(persistenceType: PersistenceTypes): AbstractSerializable {
        return when (persistenceType) {
            PersistenceTypes.CACHE -> cache
            PersistenceTypes.CONFIG -> config
        }
    }

    operator fun set(persistenceType: PersistenceTypes, value: AbstractSerializable) {
        when (persistenceType) {
            PersistenceTypes.CACHE -> cache = value as SpotilistCache
            PersistenceTypes.CONFIG -> config = value as SpotilistConfig
        }
    }
}
