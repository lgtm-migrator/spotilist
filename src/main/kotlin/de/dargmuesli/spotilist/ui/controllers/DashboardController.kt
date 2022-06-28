package de.dargmuesli.spotilist.ui.controllers

import de.dargmuesli.spotilist.MainApp
import de.dargmuesli.spotilist.models.PlaylistMapping
import de.dargmuesli.spotilist.persistence.SpotilistCache
import de.dargmuesli.spotilist.ui.SpotilistStage
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
        SpotilistCache.playlistMappings.add(PlaylistMapping())
        updatePlaylistMappings()
    }

    @FXML
    private fun menuFileSettingsAction() {
        SpotilistStage("/de/dargmuesli/spotilist/fxml/Settings.fxml", Modality.APPLICATION_MODAL, "Settings").show()
    }

    fun updatePlaylistMappings() {
        accPlaylistMappings.panes.clear()
        SpotilistCache.playlistMappings.forEach {
            val fxmlLoader =
                FXMLLoader(MainApp::class.java.getResource("fxml/PlaylistMapping.fxml"))

            accPlaylistMappings.panes.add(fxmlLoader.load())

            val fxmlController: PlaylistMappingController = fxmlLoader.getController()

            fxmlController.playlistMapping = it
        }
    }
}
