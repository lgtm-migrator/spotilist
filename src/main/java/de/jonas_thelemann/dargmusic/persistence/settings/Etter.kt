package de.jonas_thelemann.dargmusic.persistence.settings

class Etter<T, R>(var getter: () -> R, var setter: (T) -> Unit)
