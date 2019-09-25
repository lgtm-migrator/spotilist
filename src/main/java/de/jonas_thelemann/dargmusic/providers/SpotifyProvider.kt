package de.jonas_thelemann.dargmusic.providers

import com.wrapper.spotify.SpotifyApi
import com.wrapper.spotify.exceptions.SpotifyWebApiException
import com.wrapper.spotify.exceptions.detailed.NotFoundException
import com.wrapper.spotify.model_objects.specification.Playlist
import com.wrapper.spotify.model_objects.specification.PlaylistTrack
import org.apache.logging.log4j.LogManager

import java.io.IOException

object SpotifyProvider : AbstractDargmusicProvider<Playlist, PlaylistTrack>() {
    override fun getPlaylistId(playlist: Playlist): String {
        return playlist.id
    }

    override fun getPlaylistName(playlist: Playlist): String {
        return playlist.name
    }

    override fun isPlaylistIdValid(playlistId: String): Boolean {
        val errorMessage = "Playlist validation failed!"

        return try {
            SpotifyApi.builder().build().getPlaylist(playlistId).build().execute()
            true
        } catch (e: IOException) {
            LogManager.getLogger().error(errorMessage, e)
            false
        } catch (e: SpotifyWebApiException) {
            if (e !is NotFoundException) {
                LogManager.getLogger().error("$errorMessage SpotifyWebApiException is not a NotFoundException.", e)
            }

            false
        }
    }
}
