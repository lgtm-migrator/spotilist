package de.jonas_thelemann.dargmusic.modules;

public interface IDargmusicModule<PT, TT> {
  String getPlaylistId(PT playlist);
  String getPlaylistName(PT playlist);
}
