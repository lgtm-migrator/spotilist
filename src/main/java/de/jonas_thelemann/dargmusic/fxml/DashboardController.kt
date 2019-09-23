package de.jonas_thelemann.dargmusic.fxml

import de.jonas_thelemann.dargmusic.DargmusicStage
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.layout.VBox
import javafx.stage.Modality

import java.net.URL
import java.util.ResourceBundle

class DashboardController : Initializable {

    @FXML
    private lateinit var vbxPlaylistMappings: VBox

    override fun initialize(url: URL?, rb: ResourceBundle?) {
    }

    @FXML
    private fun menuFileSettingsAction() {
        DargmusicStage("fxml/Settings.fxml", Modality.APPLICATION_MODAL).showStyled("css/styles.css")
    }
}
