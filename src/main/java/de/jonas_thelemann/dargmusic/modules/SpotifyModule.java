package de.jonas_thelemann.dargmusic.modules;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.exceptions.detailed.NotFoundException;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;

public class SpotifyModule extends AbstractDargmusicModule<Playlist, PlaylistTrack> {
    private static SpotifyModule instance;

    public static SpotifyModule getInstance() {
        if (instance == null) {
            instance = new SpotifyModule();
        }

        return instance;
    }

    @Override
    public String getPlaylistId(Playlist playlist) {
        return playlist.getId();
    }

    @Override
    public String getPlaylistName(Playlist playlist) {
        return playlist.getName();
    }
}
