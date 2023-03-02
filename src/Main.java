import managers.file.FileBackedTasksManager;
import managers.file.ManagerSaveException;
import managers.util.Managers;
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
        createTasks();

        //fileManager.getTask(4);
        //System.out.println(fileManager.getHistory());


        /*System.out.println(fileManager.getHistory());
        System.out.println(fileManager.getListAllSubtasks());
        System.out.println(fileManager.getListAllTasks());
        System.out.println(fileManager.getListAllEpics());*/


    }

    private static void createTasks() {
        fileManager.createEpic("Эпик 1", "Описание", Status.DONE, "14:00, 12.07.21", "1"); // id = 0
        fileManager.createSubtask("Подзадача 1", "Описание", Status.NEW, 0, "14:00, 11.07.21", "1"); // id = 1
        fileManager.createSubtask("Подзадача 2", "Описание", Status.NEW, 0, "14:00, 10.07.21", "1"); // id = 2
        fileManager.createSubtask("Подзадача 3", "Описание", Status.IN_PROGRESS, 0, "14:00, 09.07.21", "1"); // id = 3
        fileManager.createTask("Task", "Описание", Status.IN_PROGRESS, "14:00, 08.07.21", "1"); // id = 4
    }
}