package de.dargmuesli.dargmusic.util

class Etter<T>(var getter: () -> T, var setter: (T) -> Unit)
