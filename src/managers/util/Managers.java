package managers.util;

import managers.file.ManagerSaveException;
import managers.history.HistoryManager;
import managers.history.InMemoryHistoryManager;
import managers.file.FileBackedTasksManager;
import managers.task.InMemoryTaskManager;
import managers.task.TaskManager;

import java.nio.file.Path;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager FileManager(Path path) throws ManagerSaveException {
        return new FileBackedTasksManager(path);
    }
}
