package de.jonas_thelemann.dargmusic.providers

import java.nio.file.Path
import java.nio.file.Paths

object FileSystemProvider : AbstractDargmusicProvider<Path, Path>() {
    override fun getPlaylistId(playlist: Path): String {
        return playlist.normalize().toAbsolutePath().toString()
    }

    override fun getPlaylistName(playlist: Path): String {
        return playlist.fileName.toString()
    }

    override fun isPlaylistIdValid(playlistId: String): Boolean {
        // The following returns false for non-existent directories too.
        return Paths.get(playlistId).toFile().isDirectory
    }
}
