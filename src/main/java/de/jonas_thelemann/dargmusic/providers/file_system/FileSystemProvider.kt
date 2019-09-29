package de.jonas_thelemann.dargmusic.providers.file_system

import de.jonas_thelemann.dargmusic.models.music.Playlist
import de.jonas_thelemann.dargmusic.models.music.Track
import de.jonas_thelemann.dargmusic.providers.IDargmusicProvider
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
