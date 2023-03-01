import managers.history.HistoryManager;
import managers.util.Managers;
import managers.task.TaskManager;
import tasks.Status;
import tasks.Task;

import java.time.Duration;

public class Main {
    static TaskManager taskManager = Managers.getDefault();

    public static void main(String[] args) {
        createTasks();

        //System.out.println(taskManager.getPrioritizedTasks());

        for (Task task : taskManager.getPrioritizedTasks()) {
            System.out.println(task.getId());
        }



    }
    private static void createTasks() {
        taskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77"); // id = 0
        taskManager.createSubtask("name", "description", Status.NEW,  0, "14:09, 12.07.21", "20"); // id = 1
        taskManager.createSubtask("name", "description", Status.NEW,  0, "13:09, 12.07.21", "55"); // id = 2
        taskManager.createSubtask("name", "description", Status.NEW,  0, "15:41, 12.07.21", "55"); // id = 3
        taskManager.createSubtask("name", "description", Status.NEW,  0, "12:00, 13.07.21", "55"); // id = 4
    }
}
