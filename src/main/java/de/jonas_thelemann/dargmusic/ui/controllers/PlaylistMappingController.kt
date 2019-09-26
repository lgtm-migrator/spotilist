package de.jonas_thelemann.dargmusic.ui.controllers

import de.jonas_thelemann.dargmusic.MainApp
import de.jonas_thelemann.dargmusic.models.PlaylistMapping
import de.jonas_thelemann.dargmusic.models.enums.DargmusicProvider
import de.jonas_thelemann.dargmusic.persistence.state.DargmusicState
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import javafx.scene.control.TitledPane
import javafx.scene.layout.GridPane
import java.net.URL
import java.util.*
import kotlin.properties.Delegates

class PlaylistMappingController : Initializable {

    @FXML
    private lateinit var tldpnPlaylistMapping: TitledPane
    @FXML
    private lateinit var grdpnPlaylistMappig: GridPane
    @FXML
    private lateinit var txtName: TextField
    @FXML
    private lateinit var cmbSourceProvider: ComboBox<DargmusicProvider>
    @FXML
    private lateinit var txtSourceId: TextField
    @FXML
    private lateinit var cmbTargetProvider: ComboBox<DargmusicProvider>
    @FXML
    private lateinit var txtTargetId: TextField
    @FXML
    private lateinit var btnUseEdit: Button

    var playlistMapping: PlaylistMapping by Delegates.observable(PlaylistMapping()) { _, _, newValue ->
        tldpnPlaylistMapping.text = newValue.name
        txtName.text = newValue.name
        cmbSourceProvider.value = newValue.sourceProvider
        txtSourceId.text = newValue.sourceId
        cmbTargetProvider.value = newValue.targetProvider
        txtTargetId.text = newValue.targetId
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        val providerList: ObservableList<DargmusicProvider> = FXCollections.observableArrayList()

        DargmusicProvider.values().forEach {
            providerList.add(it)
        }

        val cmbInputToEtterMap = mapOf<ComboBox<DargmusicProvider>, (DargmusicProvider) -> Unit>(
                cmbSourceProvider to { it -> playlistMapping.sourceProvider = it },
                cmbTargetProvider to { it -> playlistMapping.targetProvider = it }
        )

        for ((combobox, etter) in cmbInputToEtterMap) {
            combobox.items = providerList
            combobox.valueProperty().addListener { _, _, newValue ->
                etter.invoke(newValue)
                btnUseEdit.isDisable = !playlistMapping.validate()
            }
        }

        val txtInputToEtterMap = mapOf<TextField, (String) -> Unit>(
                txtName to { it -> playlistMapping.name = it },
                txtSourceId to { it -> playlistMapping.sourceId = it },
                txtTargetId to { it -> playlistMapping.targetId = it }
        )

        for ((textField, etter) in txtInputToEtterMap) {
            textField.textProperty().addListener { _, _, newText ->
                etter.invoke(newText)
                btnUseEdit.isDisable = !playlistMapping.validate()
            }
        }

        txtName.textProperty().addListener { _, _, newValue -> tldpnPlaylistMapping.text = newValue }
    }

    @FXML
    private fun delete() {
        DargmusicState.data.playlistMappings.remove(playlistMapping)
        MainApp.dashboardController.updatePlaylistMappings()
    }

    @FXML
    private fun toggleUseEdit() {
        if (btnUseEdit.text == "Use") {
            btnUseEdit.text = "Edit"
            grdpnPlaylistMappig.isDisable = true
        } else if (btnUseEdit.text == "Edit") {
            btnUseEdit.text = "Use"
            grdpnPlaylistMappig.isDisable = false
        }
    }
}
