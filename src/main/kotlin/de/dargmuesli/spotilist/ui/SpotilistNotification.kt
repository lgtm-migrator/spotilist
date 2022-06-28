package de.dargmuesli.spotilist.ui

import de.dargmuesli.spotilist.ui.controllers.NotificationController
import javafx.stage.Modality
import org.apache.logging.log4j.LogManager

object SpotilistNotification {
    fun displayError(text: String, e: Exception) {
        LogManager.getLogger().error(text, e)
        displayPopup(text, "Error")
    }

    fun displayInformation(text: String) {
        LogManager.getLogger().info(text)
        displayPopup(text, "Information")
    }

    fun displayWarning(text: String, e: Exception) {
        LogManager.getLogger().warn(text, e)
        displayPopup(text, "Warning")
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