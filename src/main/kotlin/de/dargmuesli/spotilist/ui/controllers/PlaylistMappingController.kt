package de.dargmuesli.spotilist.ui.controllers

import de.dargmuesli.spotilist.models.PlaylistMapping
import de.dargmuesli.spotilist.persistence.Persistence
import de.dargmuesli.spotilist.persistence.PersistenceTypes
import de.dargmuesli.spotilist.persistence.SpotilistCache
import de.dargmuesli.spotilist.persistence.cache.SpotifyCache
import de.dargmuesli.spotilist.providers.SpotilistProviderType
import javafx.collections.FXCollections.observableArrayList
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
    private lateinit var playlistMappingTitledPane: TitledPane

    @FXML
    private lateinit var playlistMappingGridPane: GridPane

    @FXML
    private lateinit var nameTextField: TextField

    @FXML
    private lateinit var sourceProviderCombobox: ComboBox<String>

    @FXML
    private lateinit var sourceIdTextField: TextField

    @FXML
    private lateinit var targetProviderCombobox: ComboBox<String>

    @FXML
    private lateinit var targetIdTextField: TextField

    @FXML
    private lateinit var useEditButton: Button

    @FXML
    private lateinit var dataLabel: Label

    var playlistMapping: PlaylistMapping by Delegates.observable(PlaylistMapping()) { _, _, newValue ->
        playlistMappingTitledPane.text = newValue.name.value
        nameTextField.text = newValue.name.value
        sourceProviderCombobox.value = newValue.sourceResource.provider.value
        sourceIdTextField.text = newValue.sourceResource.id.value
        targetProviderCombobox.value = newValue.targetResource.provider.value
        targetIdTextField.text = newValue.targetResource.id.value

        newValue.sourceResource.isValid.addListener { _ ->
            updateEditButton()
        }

        newValue.targetResource.isValid.addListener { _ ->
            updateEditButton()
        }

        newValue.isEnabled.addListener { _ ->
            if (playlistMapping.isEnabled.value) {
                useEditButton.text = "Edit"
                playlistMappingGridPane.isDisable = true
                updateData()
            } else {
                useEditButton.text = "Use"
                playlistMappingGridPane.isDisable = false
                dataLabel.text = ""
            }
        }

        updateEditButton()
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        SpotifyCache.accessToken.addListener { _ ->
            updateEditButton()
        }

        val providerList: ObservableList<String> = observableArrayList()

        SpotilistProviderType.values().forEach {
            if (SpotilistProviderType.isValid(it)) {
                providerList.add(it.name)
            }
        }

        val cmbInputToSetterMap = mapOf<ComboBox<String>, (String) -> Unit>(
            sourceProviderCombobox to { playlistMapping.sourceResource.provider.set(it) },
            targetProviderCombobox to { playlistMapping.targetResource.provider.set(it) }
        )

        for ((combobox, setter) in cmbInputToSetterMap) {
            combobox.items = providerList
            combobox.valueProperty().addListener { _, _, newValue ->
                setter.invoke(newValue)
            }
        }

        val txtInputToSetterMap = mapOf<TextField, (String) -> Unit>(
            nameTextField to { playlistMapping.name.set(it) },
            sourceIdTextField to { playlistMapping.sourceResource.id.set(it) },
            targetIdTextField to { playlistMapping.targetResource.id.set(it) }
        )

        for ((textField, setter) in txtInputToSetterMap) {
            textField.textProperty().addListener { _, _, newText ->
                setter.invoke(newText)
            }
        }

        nameTextField.textProperty().addListener { _, _, newValue ->
            playlistMappingTitledPane.text = newValue
        }
    }

    private fun updateEditButton() {
        useEditButton.isDisable =
            !playlistMapping.sourceResource.isValid.value || !playlistMapping.targetResource.isValid.value
    }

    @FXML
    private fun delete() {
        SpotilistCache.playlistMappings.remove(playlistMapping)
    }

    @FXML
    private fun toggleUseEdit() {
        playlistMapping.isEnabled.set(!playlistMapping.isEnabled.value)
        Persistence.save(PersistenceTypes.CACHE)
    }

    private fun updateData() {
        dataLabel.text = "Updating data..."

        val sourcePlaylist =
            SpotilistProviderType.valueOf(playlistMapping.sourceResource.provider.value).type.getPlaylist(
                playlistMapping.sourceResource.id.value
            )
        val targetPlaylist =
            SpotilistProviderType.valueOf(playlistMapping.targetResource.provider.value).type.getPlaylist(
                playlistMapping.targetResource.id.value
            )

        if (sourcePlaylist == null || targetPlaylist == null) return

        dataLabel.text = "Source playlist name: " + sourcePlaylist.name +
                "\nTarget playlist name: " + targetPlaylist.name +
                "\nSource playlist track count: " + sourcePlaylist.tracks?.size +
                "\nTarget playlist track count: " + targetPlaylist.tracks?.size
    }
}
