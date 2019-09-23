package de.jonas_thelemann.dargmusic

import de.jonas_thelemann.dargmusic.persistence.Persistence
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import org.apache.logging.log4j.LogManager

import java.io.IOException
import java.lang.Exception
import kotlin.system.exitProcess

class MainApp : Application() {

    override fun start(stage: Stage) {
        try {
            Persistence.loadSettings()
        } catch (e: Exception) {
            LogManager.getLogger().error("Loading application settings failed!", e)
        }

        DargmusicStage.makeDargmusicStage(stage)

        Companion.stage = stage

        stage.setOnCloseRequest {
            Persistence.saveSettings()
            exitProcess(0)
        }

        try {
            val dashboard = FXMLLoader.load<Parent>(javaClass.getResource("fxml/Dashboard.fxml"))
            val scene = Scene(dashboard)

            stage.scene = scene
            stage.show()
        } catch (e: IOException) {
            LogManager.getLogger().error("Loading the dashboard failed!", e)
        }
    }

    companion object {
        internal const val APPLICATION_TITLE = "Dargmusic"

        lateinit var stage: Stage

        @JvmStatic
        fun main(args: Array<String>) {
            launch(MainApp::class.java, *args)
        }
    }
}
