package de.jonas_thelemann.dargmusic.fxml

import de.jonas_thelemann.dargmusic.util.Etter
import de.jonas_thelemann.dargmusic.persistence.state.settings.spotify.SpotifySettings
import de.jonas_thelemann.dargmusic.persistence.state.settings.youtube.YouTubeSettings
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.TextField

import java.net.URL
import java.util.ResourceBundle

class SettingsController : Initializable {

    @FXML
    private lateinit var spotifyClientIdTextField : TextField
    @FXML
    private lateinit var spotifyClientSecretTextField : TextField
    @FXML
    private lateinit var youTubeApiKeyTextField : TextField

    override fun initialize(url: URL?, rb: ResourceBundle?) {
        val inputToEtterMap = mapOf<TextField, Etter<String, String>>(
                spotifyClientIdTextField to Etter({ SpotifySettings.clientId }, { SpotifySettings.clientId = it }),
                spotifyClientSecretTextField to Etter({ SpotifySettings.clientSecret }, { SpotifySettings.clientSecret = it }),
                youTubeApiKeyTextField to Etter({ YouTubeSettings.apiKey }, { YouTubeSettings.apiKey = it })
        )

        for ((textField, etter) in inputToEtterMap) {
            textField.text = etter.getter.invoke()
            textField.textProperty().addListener { _, _, newText -> etter.setter.invoke(newText) }
        }
    }
}
