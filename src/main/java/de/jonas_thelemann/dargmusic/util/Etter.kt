package de.jonas_thelemann.dargmusic.util

class Etter<T>(var getter: () -> T, var setter: (T) -> Unit)
