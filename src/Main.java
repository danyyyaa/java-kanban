import managers.file.FileBackedTasksManager;
import managers.file.ManagerSaveException;
import managers.util.Managers;
import managers.task.TaskManager;
import tasks.Status;

import java.nio.file.Path;

public class Main {
    static String path = "resources/data.csv";
    static FileBackedTasksManager fileManager;

    static {
        try {
            fileManager = Managers.FileManager(Path.of(path));
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        //createTasks();

        /*fileManager.getSubtask(2);
        fileManager.getTask(4);
        fileManager.getSubtask(1);
        fileManager.getEpic(5);
        fileManager.getTask(9);*/
        //fileManager.getEpic(0);
       // fileManager.getTask(4);
        //fileManager.getSubtask(1);

        System.out.println(fileManager.getHistory());
        /*System.out.println(fileManager.getListAllTasks());
        System.out.println(fileManager.getListAllSubtasks());
        System.out.println(fileManager.getListAllEpics());*/
    }
    private static void createTasks() {
        fileManager.createEpic("Эпик 1", "Описание", Status.DONE); // id = 0
        fileManager.createSubtask("Подзадача 1", "Описание", Status.NEW, 0); // id = 1
        fileManager.createSubtask("Подзадача 2", "Описание", Status.NEW, 0); // id = 2
        fileManager.createSubtask("Подзадача 3", "Описание", Status.IN_PROGRESS, 0); // id = 3
        fileManager.createTask("Task", "Описание", Status.IN_PROGRESS); // id = 4
    }
}
