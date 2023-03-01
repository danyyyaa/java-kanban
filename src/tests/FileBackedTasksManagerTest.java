/*
package tests;

import managers.file.FileBackedTasksManager;
import managers.file.ManagerSaveException;
import managers.util.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.nio.file.Path;

class FileBackedTasksManagerTest extends InMemoryTaskManagerTest {
    private FileBackedTasksManager fileManager;

    @BeforeEach
    public void createe() {
        try {
            final String path = "src/tests/resources/tests data.csv";
            fileManager = Managers.FileManager(Path.of(path));
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void edfsgsdfg() {
        fileManager.getEpic(0);
    }

}*/
