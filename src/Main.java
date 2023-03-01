import managers.history.HistoryManager;
import managers.util.Managers;
import managers.task.TaskManager;
import tasks.Status;

import java.time.Duration;

public class Main {
    static TaskManager taskManager = Managers.getDefault();

    public static void main(String[] args) {
        createTasks();

        System.out.println(taskManager.getEpic(0));
        System.out.println(taskManager.getEpic(0).getStartDate());
        System.out.println(taskManager.getEpic(0).getDuration());
        System.out.println(taskManager.getEpic(0).getEndTime());




    }
    private static void createTasks() {
        taskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        taskManager.createTask("name", "description", Status.NEW, "14:09, 12.07.21", "25");
        taskManager.createSubtask("name", "description", Status.NEW,  0, "14:09, 12.07.21", "55");
    }
}
