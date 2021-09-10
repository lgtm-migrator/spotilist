package de.dargmuesli.dargmusic

import de.dargmuesli.dargmusic.persistence.Persistence
import de.dargmuesli.dargmusic.ui.DargmusicStage
import de.dargmuesli.dargmusic.ui.controllers.DashboardController
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import org.apache.logging.log4j.LogManager

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
            val dashboardLoader = FXMLLoader(javaClass.getResource("/de/dargmuesli/dargmusic/fxml/Dashboard.fxml"))
            val dashboard = dashboardLoader.load<Parent>()
            dashboardController = dashboardLoader.getController()

            val scene = Scene(dashboard)

            stage.scene = scene
            stage.show()
        } catch (e: IOException) {
            LogManager.getLogger().error("Loading the dashboard failed!", e)
        }
    }

    companion object {
        lateinit var stage: Stage
        lateinit var dashboardController: DashboardController

        internal const val APPLICATION_TITLE = "Dargmusic"

        @JvmStatic
        fun main(args: Array<String>) {
            launch(MainApp::class.java, *args)
        }

        fun isStageInitialized() = this::stage.isInitialized
    }
}
