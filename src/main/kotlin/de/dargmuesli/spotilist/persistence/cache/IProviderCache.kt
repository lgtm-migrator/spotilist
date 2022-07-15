package de.dargmuesli.spotilist.persistence.cache

import javafx.collections.ObservableMap

interface IProviderCache<PT, TT> : IClearable {
    var playlistData: ObservableMap<String, PT>
    var playlistItemData: ObservableMap<String, TT>

    override fun clear() {
        for (entry in playlistData) {
            playlistData.remove(entry.key)
        }

        for (entry in playlistItemData) {
            playlistItemData.remove(entry.key)
        }
    }
}