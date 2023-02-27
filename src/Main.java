import managers.file.FileBackedTasksManager;
import managers.file.ManagerSaveException;
import managers.task.TaskManager;
import managers.util.Managers;
import tasks.Status;

import java.nio.file.Path;

public class Main {
    static TaskManager taskManager = Managers.getDefault();

    public static void main(String[] args) {
        createTasks();
        taskManager.calculateEpicStatus(0);
        System.out.println(taskManager.getListAllEpics());

    }
    private static void createTasks() {
        taskManager.createEpic("Эпик 1", "Описание", Status.NEW); // id = 0
        taskManager.createSubtask("Подзадача 1", "Описание", Status.DONE, 0); // id = 1
        taskManager.createSubtask("Подзадача 2", "Описание", Status.DONE, 0); // id = 2
    }
}
