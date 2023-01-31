package managers.util;

import managers.history.HistoryManager;
import managers.history.InMemoryHistoryManager;
import managers.task.InMemoryClassTaskManager;
import managers.task.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryClassTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
