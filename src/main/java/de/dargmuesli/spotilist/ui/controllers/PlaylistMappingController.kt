package de.dargmuesli.spotilist.ui.controllers

import de.dargmuesli.spotilist.MainApp
import de.dargmuesli.spotilist.models.PlaylistMapping
import de.dargmuesli.spotilist.models.enums.SpotilistProvider
import de.dargmuesli.spotilist.persistence.state.SpotilistState
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
    private lateinit var cmbSourceProvider: ComboBox<SpotilistProvider>

    @FXML
    private lateinit var txtSourceId: TextField

    @FXML
    private lateinit var cmbTargetProvider: ComboBox<SpotilistProvider>

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
        val providerList: ObservableList<SpotilistProvider> = FXCollections.observableArrayList()

        SpotilistProvider.values().forEach {
            if (SpotilistProvider.isValid(it)) {
                providerList.add(it)
            }
        }

        val cmbInputToEtterMap = mapOf<ComboBox<SpotilistProvider>, (SpotilistProvider) -> Unit>(
            cmbSourceProvider to { playlistMapping.sourceResource.provider = it },
            cmbTargetProvider to { playlistMapping.targetResource.provider = it }
        )

        for ((combobox, etter) in cmbInputToEtterMap) {
            combobox.items = providerList
            combobox.valueProperty().addListener { _, _, newValue ->
                etter.invoke(newValue)
                btnUseEdit.isDisable = !playlistMapping.validate()
            }
        }

        val txtInputToEtterMap = mapOf<TextField, (String) -> Unit>(
            txtName to { playlistMapping.name = it },
            txtSourceId to { playlistMapping.sourceResource.id = it },
            txtTargetId to { playlistMapping.targetResource.id = it }
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
        SpotilistState.data.playlistMappings.remove(playlistMapping)
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
        val sourcePlaylist = SpotilistProvider.getPlaylist(playlistMapping.sourceResource)
        val targetPlaylist = SpotilistProvider.getPlaylist(playlistMapping.targetResource)

        lblData.text = "Source playlist name: " + sourcePlaylist.name +
                "\nTarget playlist name: " + targetPlaylist.name +
                "\nSource playlist track count: " + sourcePlaylist.tracks.size +
                "\nTarget playlist track count: " + targetPlaylist.tracks.size
    }
}
