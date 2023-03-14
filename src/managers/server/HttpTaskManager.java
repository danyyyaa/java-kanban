package managers.server;

import managers.file.FileBackedTasksManager;
import managers.file.ManagerSaveException;

import java.nio.file.Path;

public class HttpTaskManager extends FileBackedTasksManager {
    public HttpTaskManager(Path path) throws ManagerSaveException {
        super(path);
    }
}
