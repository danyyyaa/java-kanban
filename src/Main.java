import managers.file.FileBackedTasksManager;
import managers.file.ManagerSaveException;
import managers.util.Managers;
import managers.task.TaskManager;
import tasks.Status;

import java.nio.file.Path;

public class Main {

    //static TaskManager taskManager = Managers.getDefault();
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
        //fileManager.createTask("name", "description", Status.DONE);

        //fileManager.removeEpicById(0);

        /*fileManager.getEpic(0); // запрос задач
        fileManager.getEpic(4);

        fileManager.getHistory(); // 0 4
        System.out.println();

        fileManager.getSubtask(1);
        fileManager.getSubtask(2);

        System.out.println(fileManager.getHistory()); // 0 4 1 2

        fileManager.getSubtask(3);

        System.out.println(fileManager.getHistory()); // 0 4 1 2 3

        fileManager.removeSubtaskById(2);
        System.out.println(fileManager.getHistory()); // 0 4 1 3

        fileManager.removeEpicById(0); // удаление эпика с 3 подзадачами*/
        //System.out.println("\n" + fileManager.getHistory()); // 4
        //fileManager.removeSubtaskById(1);
        fileManager.getSubtask(2);
        fileManager.getSubtask(3);
        fileManager.getEpic(0);

        System.out.println("11history: " + fileManager.getHistory());

        /*System.out.println(fileManager.getListAllTasks());
        System.out.println(fileManager.getListAllSubtasks());
        System.out.println(fileManager.getListAllEpics());*/
    }
    private static void createTasks() {
        fileManager.createEpic("Эпик 1", "Описание", Status.NEW); // id = 0
        fileManager.createSubtask("Подзадача 1", "Описание", Status.NEW, 0); // id = 1
        fileManager.createSubtask("Подзадача 2", "Описание", Status.NEW, 0); // id = 2
        fileManager.createSubtask("Подзадача 3", "Описание", Status.NEW, 0); // id = 3

        /*fileManager.createEpic("Эпик 2", "Описание", Status.NEW); // id = 4

        fileManager.createTask("Task1", "Описание", Status.NEW); // id = 5
        fileManager.createTask("Task2", "Описание", Status.NEW); // id = 6*/
    }
}
