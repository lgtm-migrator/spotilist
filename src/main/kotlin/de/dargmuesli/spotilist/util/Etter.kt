package de.dargmuesli.spotilist.util

class Etter<T>(var getter: () -> T, var setter: (T) -> Unit)
