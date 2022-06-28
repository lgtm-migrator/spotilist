package de.dargmuesli.spotilist.providers.file_system

import de.dargmuesli.spotilist.models.music.Playlist
import de.dargmuesli.spotilist.models.music.Track
import de.dargmuesli.spotilist.providers.ISpotilistProvider
import java.nio.file.Paths

object FileSystemProvider : ISpotilistProvider {
    override fun getPlaylist(playlistId: String): Playlist {
        val path = Paths.get(playlistId)
        val playlistName = path.fileName.toString()
        val playlistTracks: MutableList<Track> = mutableListOf()

        path.toFile().listFiles { file -> file.isFile }!!.forEach { file ->
            playlistTracks.add(Track(name = file.nameWithoutExtension))
        }

        return Playlist(name = playlistName, tracks = playlistTracks)
    }

    override fun isPlaylistIdValid(playlistId: String): Boolean {
        // The following returns false for non-existent directories too.
        return Paths.get(playlistId).toFile().isDirectory
    }
}
