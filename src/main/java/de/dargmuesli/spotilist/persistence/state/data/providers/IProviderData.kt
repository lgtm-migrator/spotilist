package de.dargmuesli.spotilist.persistence.state.data.providers

interface IProviderData<PT, TT> {
    var playlistData: MutableMap<String, PT>
    var playlistItemData: MutableMap<String, TT>
}