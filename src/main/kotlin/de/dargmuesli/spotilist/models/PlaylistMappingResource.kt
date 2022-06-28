package de.dargmuesli.spotilist.models

import de.dargmuesli.spotilist.providers.SpotilistProvider
import kotlinx.serialization.Serializable

@Serializable
data class PlaylistMappingResource(
    var provider: SpotilistProvider = SpotilistProvider.NONE,
    var id: String = String()
)