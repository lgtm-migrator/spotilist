package de.jonas_thelemann.dargmusic.modules;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.FileSystems;
import java.nio.file.Paths;

class FileSystemModuleTest {
    private FileSystemModule fileSystemModule = new FileSystemModule();

    @Test
    void getPlaylistIdTest() {
        Assertions.assertEquals(FileSystems.getDefault().getPath("").toAbsolutePath().resolve("path").toString(),
                fileSystemModule.getPlaylistId(Paths.get("test", "..", "path")));
    }

    @Test
    void getPlaylistNameTest() {
        Assertions.assertEquals("path",
                fileSystemModule.getPlaylistName(Paths.get("test", "path")));
    }
}
