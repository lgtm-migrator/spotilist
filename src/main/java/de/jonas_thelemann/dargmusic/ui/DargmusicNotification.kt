package de.jonas_thelemann.dargmusic.ui

import de.jonas_thelemann.dargmusic.ui.controllers.NotificationController
import javafx.stage.Modality
import org.apache.logging.log4j.LogManager

object DargmusicNotification {
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
        DargmusicStage("../fxml/Notification.fxml", Modality.APPLICATION_MODAL, title, true)
                .showStyled("../css/styles.css", true)
    }
}