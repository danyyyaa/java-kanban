import managers.history.HistoryManager;
import managers.util.Managers;
import managers.task.TaskManager;
import tasks.Status;

public class Main {
    static TaskManager taskManager = Managers.getDefault();

    public static void main(String[] args) {
        createTasks();



    }
    private static void createTasks() {
        //taskManager.createTask("name", "description", Status.NEW, );
    }
}
