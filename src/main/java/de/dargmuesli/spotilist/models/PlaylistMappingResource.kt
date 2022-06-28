package de.dargmuesli.spotilist.models

import de.dargmuesli.spotilist.models.enums.SpotilistProvider

data class PlaylistMappingResource(
    var provider: SpotilistProvider = SpotilistProvider.NONE,
    var id: String = String()
)