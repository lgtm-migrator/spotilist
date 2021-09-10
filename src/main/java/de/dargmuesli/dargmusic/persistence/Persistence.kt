package de.dargmuesli.dargmusic.persistence

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.model.PlaylistItem
import de.dargmuesli.dargmusic.persistence.state.DargmusicStateWrapper
import de.dargmuesli.dargmusic.ui.DargmusicNotification
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.logging.Logger.getGlobal
import kotlin.system.exitProcess

object Persistence {
    private val appDataDirectory: Path
        get() {
            val os = System.getProperty("os.name").lowercase()

            return if (os.contains("win")) {
                Paths.get(System.getenv("AppData"), "Dargmusic")
            } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
                Paths.get(System.getProperty("user.home"), ".config", "Dargmusic")
            } else {
                Paths.get("")
            }
        }
    private val jackson: ObjectMapper = ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)
    private val settingsFile = appDataDirectory.resolve("settings.json")

    fun loadSettings() {
        if (Files.exists(settingsFile)) {
            try {
                DargmusicStateWrapper.state = jackson.readValue(String(Files.readAllBytes(settingsFile)), DargmusicStateWrapper.javaClass).state
            } catch (e: Exception) {
                DargmusicNotification.displayError("Loading application settings failed!", e)
                exitProcess(0)
            }
        }
    }

    fun saveSettings() {
        if (!Files.exists(settingsFile.parent)) {
            Files.createDirectories(settingsFile.parent)
        }

        Files.writeString(settingsFile, jackson.writeValueAsString(DargmusicStateWrapper))
    }

    fun getCacheDirectory(): Path {
        return Paths.get(System.getProperty("java.io.tmpdir"), "Dargmusic")
    }

    fun loadPlaylistItemsCache(cacheDirectory: Path): Map<String, PlaylistItem> {
        val playlistItems = HashMap<String, PlaylistItem>()
        val properties = Properties()

        if (Files.isRegularFile(cacheDirectory)) {
            try {
                properties.load(Files.newInputStream(cacheDirectory))
            } catch (e: IOException) {
                println("Could not load YouTubeSettings cache.")
            }

        }

        for (key in properties.stringPropertyNames()) {
            try {
                playlistItems[key] = JacksonFactory().createJsonParser(properties[key].toString()).parse(PlaylistItem::class.java)
            } catch (e: IOException) {
                println("Could not parse \"" + properties[key].toString() + "\".")
            }

        }

        return playlistItems
    }

    fun savePlaylistItems(playlistItemMap: Map<String, PlaylistItem>, cacheDirectory: Path) {
        val properties = Properties()

        for ((key, value) in playlistItemMap) {
            properties[key] = value.toString()
        }

        val cacheDirectoryParentFile = cacheDirectory.parent

        if (!Files.isRegularFile(cacheDirectoryParentFile)) {
            getGlobal().info("Created directory: " + Files.createDirectories(cacheDirectoryParentFile))
        }

        properties.store(Files.newOutputStream(cacheDirectory), null)
    }
}
