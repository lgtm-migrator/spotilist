package de.jonas_thelemann.dargmusic.util

class Etter<T, R>(var getter: () -> R, var setter: (T) -> Unit)
