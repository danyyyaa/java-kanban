import managers.history.HistoryManager;
import managers.util.Managers;
import managers.task.TaskManager;
import tasks.Status;

import java.time.Duration;

public class Main {
    static TaskManager taskManager = Managers.getDefault();

    public static void main(String[] args) {
        createTasks();

        System.out.println(taskManager.getEpic(0).getStartTime());
        System.out.println(taskManager.getEpic(0).getEndTime());
        taskManager.calculateEpicTime(0);
        System.out.println(taskManager.getEpic(0).getStartTime());
        System.out.println(taskManager.getEpic(0).getEndTime());





    }
    private static void createTasks() {
        taskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        taskManager.createSubtask("name", "description", Status.NEW,  0, "14:09, 12.07.21", "20");
        taskManager.createSubtask("name", "description", Status.NEW,  0, "13:09, 12.07.21", "55");
        taskManager.createSubtask("name", "description", Status.NEW,  0, "15:41, 12.07.21", "55");
        taskManager.createSubtask("name", "description", Status.NEW,  0, "12:00, 12.07.21", "55");
    }
}
