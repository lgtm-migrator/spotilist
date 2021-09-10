package de.dargmuesli.dargmusic.ui.controllers

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

class NotificationController : Initializable {

    @FXML
    lateinit var lblNotification: Label

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        lblNotification.text = notifications.removeAt(0)
    }

    companion object {
        var notifications: ArrayList<String> = ArrayList()
    }
}
