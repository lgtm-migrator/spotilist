package de.jonas_thelemann.dargmusic

import de.jonas_thelemann.dargmusic.persistence.Persistence
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

import java.io.IOException
import kotlin.system.exitProcess

class MainApp : Application() {

    override fun start(stage: Stage) {
        Persistence.loadSettings()
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
            e.printStackTrace()
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
