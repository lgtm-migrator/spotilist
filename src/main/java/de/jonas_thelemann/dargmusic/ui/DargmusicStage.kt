package de.jonas_thelemann.dargmusic.ui

import de.jonas_thelemann.dargmusic.MainApp
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Modality
import javafx.stage.Stage
import org.apache.logging.log4j.LogManager

import java.io.IOException

class DargmusicStage(fxmlPath: String, modality: Modality, title: String, isAlwaysOnTop: Boolean = false) : Stage() {
    init {
        try {
            val dashboard = FXMLLoader.load<Parent>(javaClass.getResource(fxmlPath))
            val scene = Scene(dashboard)

            this.scene = scene
            this.title = MainApp.APPLICATION_TITLE + " - " + title
            this.icons.add(Image(javaClass.getResourceAsStream("../icons/icon.png")))
            this.initModality(modality)
            this.isAlwaysOnTop = true

            if (MainApp.isStageInitialized()) {
                this.initOwner(MainApp.stage)
            } else {
                this.initOwner(Stage())
            }
        } catch (e: IOException) {
            LogManager.getLogger().error("Construction of DargmusicStage failed!", e)
        }
    }

    fun showStyled(cssPath: String, andWait: Boolean = false) {
        this.scene.stylesheets.add(javaClass.getResource(cssPath).toExternalForm())

        if (andWait) {
            this.showAndWait()
        } else {
            this.show()
        }
    }

    companion object {
        internal fun makeDargmusicStage(stage: Stage) {
            stage.title = MainApp.APPLICATION_TITLE
            stage.icons.add(Image(MainApp().javaClass.getResourceAsStream("icons/icon.png")))
        }
    }
}
