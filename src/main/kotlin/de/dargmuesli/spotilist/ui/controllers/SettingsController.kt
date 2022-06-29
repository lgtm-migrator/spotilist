package de.dargmuesli.spotilist.ui.controllers

import de.dargmuesli.spotilist.persistence.config.SpotifyConfig
import de.dargmuesli.spotilist.persistence.config.YouTubeConfig
import de.dargmuesli.spotilist.providers.spotify.SpotifyProvider
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.TextField
import java.net.MalformedURLException
import java.net.URISyntaxException
import java.net.URL
import java.util.*


class SettingsController : Initializable {

    @FXML
    private lateinit var spotifyClientIdTextField: TextField

    @FXML
    private lateinit var spotifyClientSecretTextField: TextField

    @FXML
    private lateinit var spotifyRedirectUriTextField: TextField

    @FXML
    private lateinit var openAuthorizationButton: Button

    @FXML
    private lateinit var youTubeApiKeyTextField: TextField

    override fun initialize(url: URL?, rb: ResourceBundle?) {
        spotifyClientIdTextField.text = SpotifyConfig.clientId.value
        spotifyClientSecretTextField.text = SpotifyConfig.clientSecret.value
        spotifyRedirectUriTextField.text = SpotifyConfig.redirectUri.value
        youTubeApiKeyTextField.text = YouTubeConfig.apiKey.value

        openAuthorizationButton.isDisable = !isAuthorizable()
    }

    @FXML
    private fun onSpotifyClientIdInput() {
        SpotifyConfig.clientId.set(spotifyClientIdTextField.text)
        openAuthorizationButton.isDisable = !isAuthorizable()
    }

    @FXML
    private fun onSpotifyClientSecretInput() {
        SpotifyConfig.clientSecret.set(spotifyClientSecretTextField.text)
        openAuthorizationButton.isDisable = !isAuthorizable()
    }

    @FXML
    private fun onSpotifyRedirectUriInput() {
        SpotifyConfig.redirectUri.set(spotifyRedirectUriTextField.text)
        openAuthorizationButton.isDisable = !isAuthorizable()
    }

    @FXML
    private fun onYouTubeApiKeyInput() {
        YouTubeConfig.apiKey.set(youTubeApiKeyTextField.text)
    }

    @FXML
    private fun openAuthorization() {
        SpotifyProvider.openAuthorization()
    }

    private fun isAuthorizable(): Boolean {
        if (spotifyClientIdTextField.text == ""
            || spotifyClientSecretTextField.text == ""
            || spotifyRedirectUriTextField.text == ""
        ) {
            return false
        }

        return try {
            URL(spotifyRedirectUriTextField.text).toURI()
            true
        } catch (exception: URISyntaxException) {
            false
        } catch (exception: MalformedURLException) {
            false
        }
    }
}
