package de.dargmuesli.dargmusic.ui.controllers.spotify

import de.dargmuesli.dargmusic.providers.spotify.SpotifyUtil
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.stage.Stage
import java.net.URL
import java.util.*


class SpotifyAuthorizationCodeController : Initializable {
    @FXML
    private lateinit var spotifyAuthorizationCodeTextField: TextField
    @FXML
    private lateinit var btnSave: Button

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        spotifyAuthorizationCodeTextField.textProperty().addListener { _, _, newText -> btnSave.isDisable = newText == "" }
    }

    @FXML
    private fun save() {
        SpotifyUtil.setAuthorizationCodeCredentials(spotifyAuthorizationCodeTextField.text)
        (spotifyAuthorizationCodeTextField.scene.window as Stage).close()
    }
}