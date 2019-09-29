package de.jonas_thelemann.dargmusic.ui.controllers

import de.jonas_thelemann.dargmusic.MainApp
import de.jonas_thelemann.dargmusic.models.PlaylistMapping
import de.jonas_thelemann.dargmusic.models.enums.DargmusicProvider
import de.jonas_thelemann.dargmusic.persistence.state.DargmusicState
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
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
    @FXML
    private lateinit var lblData: Label

    var playlistMapping: PlaylistMapping by Delegates.observable(PlaylistMapping()) { _, _, newValue ->
        tldpnPlaylistMapping.text = newValue.name
        txtName.text = newValue.name
        cmbSourceProvider.value = newValue.sourceResource.provider
        txtSourceId.text = newValue.sourceResource.id
        cmbTargetProvider.value = newValue.targetResource.provider
        txtTargetId.text = newValue.targetResource.id
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        val providerList: ObservableList<DargmusicProvider> = FXCollections.observableArrayList()

        DargmusicProvider.values().forEach {
            if (DargmusicProvider.isValid(it)) {
                providerList.add(it)
            }
        }

        val cmbInputToEtterMap = mapOf<ComboBox<DargmusicProvider>, (DargmusicProvider) -> Unit>(
                cmbSourceProvider to { it -> playlistMapping.sourceResource.provider = it },
                cmbTargetProvider to { it -> playlistMapping.targetResource.provider = it }
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
                txtSourceId to { it -> playlistMapping.sourceResource.id = it },
                txtTargetId to { it -> playlistMapping.targetResource.id = it }
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
            lblData.text = "Updating data..."
            updateData()
        } else if (btnUseEdit.text == "Edit") {
            btnUseEdit.text = "Use"
            grdpnPlaylistMappig.isDisable = false
            lblData.text = ""
        }
    }

    private fun updateData() {
        val sourcePlaylist = DargmusicProvider.getPlaylist(playlistMapping.sourceResource)
        val targetPlaylist = DargmusicProvider.getPlaylist(playlistMapping.targetResource)

        lblData.text = "Source playlist name: " + sourcePlaylist.name +
                "\nTarget playlist name: " + targetPlaylist.name +
                "\nSource playlist track count: " + sourcePlaylist.tracks.size +
                "\nTarget playlist track count: " + targetPlaylist.tracks.size
    }
}
