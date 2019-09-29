package de.jonas_thelemann.dargmusic.providers.file_system

import java.util.regex.Pattern

object FileSystemUtil {
    fun replaceIllegalChactersWindows(input: String, replacement: String = "_"): String {
        return Pattern.compile("[\\\\/:*?\"<>|]").matcher(input).replaceAll(replacement)
    }
}