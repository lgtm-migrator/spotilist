package de.dargmuesli.spotilist.providers.file_system

import java.util.regex.Pattern

object FileSystemUtil {
    fun replaceIllegalChactersWindows(input: String, replacement: String = String()): String {
        return Pattern.compile("[\\\\/:*?\"<>|]").matcher(input).replaceAll(replacement)
    }
}