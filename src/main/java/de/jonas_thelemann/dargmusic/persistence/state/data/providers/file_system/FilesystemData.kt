package de.jonas_thelemann.dargmusic.persistence.state.data.providers.file_system

import de.jonas_thelemann.dargmusic.persistence.state.data.providers.IProviderData
import java.nio.file.Path

object FileSystemData : IProviderData<Path, Path> {
    override var playlistData = mutableMapOf<String, Path>()
    override var playlistItemData = mutableMapOf<String, Path>()
}