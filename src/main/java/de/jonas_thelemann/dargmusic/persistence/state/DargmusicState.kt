package de.jonas_thelemann.dargmusic.persistence.state

import de.jonas_thelemann.dargmusic.persistence.state.data.PlaylistMappingState
import de.jonas_thelemann.dargmusic.persistence.state.settings.DargmusicSettings

object DargmusicState {
    var data: PlaylistMappingState = PlaylistMappingState
    var settings: DargmusicSettings = DargmusicSettings
}
