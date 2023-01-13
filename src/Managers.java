public class Managers {

    static TaskManager getDefault() {
        return new InMemoryClassTaskManager();
    }

    static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
