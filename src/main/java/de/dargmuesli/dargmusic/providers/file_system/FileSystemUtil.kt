package de.dargmuesli.dargmusic.providers.file_system

import java.util.regex.Pattern

object FileSystemUtil {
    fun replaceIllegalChactersWindows(input: String, replacement: String = String()): String {
        return Pattern.compile("[\\\\/:*?\"<>|]").matcher(input).replaceAll(replacement)
    }
}