package de.jonas_thelemann.dargmusic.ui.controllers

import de.jonas_thelemann.dargmusic.models.PlaylistMapping
import de.jonas_thelemann.dargmusic.persistence.state.DargmusicState
import de.jonas_thelemann.dargmusic.ui.DargmusicStage
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
        DargmusicState.data.playlistMappingData.playlistMappings.add(PlaylistMapping())
        updatePlaylistMappings()
    }

    @FXML
    private fun menuFileSettingsAction() {
        DargmusicStage("../fxml/Settings.fxml", Modality.APPLICATION_MODAL, "Settings")
                .showStyled()
    }

    fun updatePlaylistMappings() {
        accPlaylistMappings.panes.clear()
        DargmusicState.data.playlistMappingData.playlistMappings.forEach {
            val fxmlLoader = FXMLLoader(javaClass.getResource("../../fxml/PlaylistMapping.fxml"))

            accPlaylistMappings.panes.add(fxmlLoader.load())

            val fxmlController: PlaylistMappingController = fxmlLoader.getController()

            fxmlController.playlistMapping = it
        }
    }
}
