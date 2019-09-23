package de.jonas_thelemann.dargmusic.modules;
import java.nio.file.*;

public class FileSystemModule extends AbstractDargmusicModule<Path, Path> {
  private static FileSystemModule instance;

  public static FileSystemModule getInstance() {
    if (instance == null) {
      instance = new FileSystemModule();
    }

    return instance;
  }

  @Override
  public String getPlaylistId(Path playlist) {
    return playlist.normalize().toAbsolutePath().toString();
  }

  @Override
  public String getPlaylistName(Path playlist) {
    return playlist.getFileName().toString();
  }
}
