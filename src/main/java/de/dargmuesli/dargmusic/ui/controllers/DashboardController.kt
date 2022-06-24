package de.dargmuesli.dargmusic.ui.controllers

import de.dargmuesli.dargmusic.MainApp
import de.dargmuesli.dargmusic.models.PlaylistMapping
import de.dargmuesli.dargmusic.persistence.state.DargmusicState
import de.dargmuesli.dargmusic.ui.DargmusicStage
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.control.Accordion
import javafx.stage.Modality
import java.net.URL
import java.util.*

class DashboardController : Initializable {

    @FXML
    private lateinit var accPlaylistMappings: Accordion

    override fun initialize(url: URL?, rb: ResourceBundle?) {
        updatePlaylistMappings()
    }

    @FXML
    private fun addPlaylistMapping() {
        DargmusicState.data.playlistMappings.add(PlaylistMapping())
        updatePlaylistMappings()
    }

    @FXML
    private fun menuFileSettingsAction() {
        DargmusicStage("/de/dargmuesli/dargmusic/fxml/Settings.fxml", Modality.APPLICATION_MODAL, "Settings").show()
    }

    fun updatePlaylistMappings() {
        accPlaylistMappings.panes.clear()
        DargmusicState.data.playlistMappings.forEach {
            val fxmlLoader =
                FXMLLoader(MainApp::class.java.getResource("/de/dargmuesli/dargmusic/fxml/PlaylistMapping.fxml"))

            accPlaylistMappings.panes.add(fxmlLoader.load())

            val fxmlController: PlaylistMappingController = fxmlLoader.getController()

            fxmlController.playlistMapping = it
        }
    }
}
