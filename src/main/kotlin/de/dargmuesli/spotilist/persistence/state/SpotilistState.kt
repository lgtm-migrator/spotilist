package de.dargmuesli.spotilist.persistence.state

import de.dargmuesli.spotilist.persistence.state.data.SpotilistData
import de.dargmuesli.spotilist.persistence.state.settings.SpotilistSettings

object SpotilistState {
    var data = SpotilistData
    var settings = SpotilistSettings
}
