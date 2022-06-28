package de.dargmuesli.spotilist.ui

import de.dargmuesli.spotilist.ui.controllers.NotificationController
import javafx.stage.Modality
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.javafx.JavaFxDispatcher
import kotlinx.coroutines.launch
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object SpotilistNotification : CoroutineScope {
    private val LOGGER: Logger = LogManager.getLogger()

    override val coroutineContext: JavaFxDispatcher
        get() = Dispatchers.JavaFx

    fun error(text: String, e: Exception? = null) {
        LOGGER.error(text, e)
        launch(Dispatchers.JavaFx) {
            displayPopup(text, "Error")
        }
    }

    fun info(text: String) {
        LOGGER.info(text)
        launch(Dispatchers.JavaFx) {
            displayPopup(text, "Information")
        }
    }

    fun warn(text: String, e: Exception? = null) {
        LOGGER.warn(text, e)
        launch(Dispatchers.JavaFx) {
            displayPopup(text, "Warning")
        }
    }

    private fun displayPopup(text: String, title: String = "Notification") {
        NotificationController.notifications.add(text)
        SpotilistStage(
            "/de/dargmuesli/spotilist/fxml/Notification.fxml",
            Modality.APPLICATION_MODAL,
            title,
            true
        ).showAndWait()
    }
}