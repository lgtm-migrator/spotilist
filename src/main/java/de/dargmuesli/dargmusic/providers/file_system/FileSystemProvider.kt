package de.dargmuesli.dargmusic.providers.file_system

import de.dargmuesli.dargmusic.models.music.Playlist
import de.dargmuesli.dargmusic.models.music.Track
import de.dargmuesli.dargmusic.providers.IDargmusicProvider
import java.nio.file.Paths

object FileSystemProvider : IDargmusicProvider {
    override fun getPlaylist(playlistId: String): Playlist {
        val path = Paths.get(playlistId)
        val playlistName = path.fileName.toString()
        val playlistTracks: MutableList<Track> = mutableListOf()

        path.toFile().listFiles { file -> file.isFile }!!.forEach { file ->
            playlistTracks.add(Track(name = file.nameWithoutExtension))
        }

        return Playlist(playlistName, playlistTracks)
    }

    override fun isPlaylistIdValid(playlistId: String): Boolean {
        // The following returns false for non-existent directories too.
        return Paths.get(playlistId).toFile().isDirectory
    }
}
