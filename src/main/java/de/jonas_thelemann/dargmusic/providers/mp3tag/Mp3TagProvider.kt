package de.jonas_thelemann.dargmusic.providers.mp3tag

import com.mpatric.mp3agic.ID3v24Tag
import com.mpatric.mp3agic.Mp3File
import de.jonas_thelemann.dargmusic.models.music.Album
import de.jonas_thelemann.dargmusic.models.music.Artist
import de.jonas_thelemann.dargmusic.models.music.Playlist
import de.jonas_thelemann.dargmusic.models.music.Track
import de.jonas_thelemann.dargmusic.providers.IDargmusicProvider
import de.jonas_thelemann.dargmusic.providers.file_system.FileSystemProvider
import java.nio.file.Paths

object Mp3TagProvider : IDargmusicProvider {
    override fun getPlaylist(playlistId: String): Playlist {
        val path = Paths.get(playlistId)
        val playlistName = path.fileName.toString()
        val playlistTracks: MutableList<Track> = mutableListOf()

        path.toFile().listFiles { file -> file.isFile }!!.forEach { file ->
            val mp3File = Mp3File(file)
            val id3v2Tag = if (mp3File.hasId3v2Tag()) {
                mp3File.id3v2Tag
            } else {
                ID3v24Tag()
            }

            val trackAlbum = if (id3v2Tag.album != null) {
                Album(name = id3v2Tag.album)
            } else {
                Album()
            }

            val trackArtists = listOf(Artist(name = id3v2Tag.artist))
            val trackDurationMs = mp3File.lengthInMilliseconds
            val trackName = id3v2Tag.title

            playlistTracks.add(Track(trackAlbum, trackArtists, trackDurationMs.toInt(), trackName))
        }

        return Playlist(playlistName, playlistTracks)
    }

    override fun isPlaylistIdValid(playlistId: String): Boolean {
        return FileSystemProvider.isPlaylistIdValid(playlistId)
    }
}
