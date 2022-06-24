package de.dargmuesli.dargmusic.models

import de.dargmuesli.dargmusic.models.enums.DargmusicProvider

data class PlaylistMappingResource(
    var provider: DargmusicProvider = DargmusicProvider.NONE,
    var id: String = String()
)