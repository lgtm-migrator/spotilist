package de.jonas_thelemann.dargmusic.models

import de.jonas_thelemann.dargmusic.models.enums.DargmusicProvider

data class PlaylistMappingResource(var provider: DargmusicProvider = DargmusicProvider.NONE,
                                   var id: String = String())