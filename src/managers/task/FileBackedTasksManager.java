package managers.task;

import managers.history.HistoryManager;
import managers.util.Managers;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryClassTaskManager {
    static File file;

    static HistoryManager historyManager = Managers.getDefaultHistory();

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) throws ManagerSaveException {
        FileBackedTasksManager fileBackedTasksManager = loadFromFile(new File("123.csv"));

        fileBackedTasksManager.createEpic("Эпик 1", "Описание", Status.NEW); // id = 0
        fileBackedTasksManager.createSubtask("Подзадача 1", "Описание", Status.NEW, 0); // id = 1
        fileBackedTasksManager.createSubtask("Подзадача 2", "Описание", Status.NEW, 0); // id = 2
        fileBackedTasksManager.createSubtask("Подзадача 3", "Описание", Status.NEW, 0); // id = 3

        fileBackedTasksManager.createEpic("Эпик 2", "Описание", Status.NEW); // id = 4
        fileBackedTasksManager.createTask("Задача 1", "Описание", Status.IN_PROGRESS); // id = 5

        historyManager.add(fileBackedTasksManager.getSubtask(3));
        historyManager.add(fileBackedTasksManager.getEpic(0));
        historyManager.add(fileBackedTasksManager.getTask(5));

        System.out.println(historyManager.getHistory()); // это работает
    }

    public void save() throws ManagerSaveException {
        try (FileWriter fw = new FileWriter(file)) {

            fw.write(historyToString(historyManager));
            fw.write("gfsdgdfg");

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка ввода");
        }
    }

    static String historyToString(HistoryManager manager) {
        String history = "";
        for (Task task : manager.getHistory()) {
            history += task.getId().toString();
            System.out.println(task.getId());
        }

        return history;
    }

    static FileBackedTasksManager loadFromFile(File file) throws ManagerSaveException {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file.getAbsolutePath()))) {

            return new FileBackedTasksManager(file);
        } catch (IOException e) {
            throw new ManagerSaveException("Невозможно прочитать файл. Возможно файл не находится в нужной директории.");
        }
    }

    /*String toString(Task task) {
        return null;
    }*/

    Task fromString(String value) {
        return null;
    }



    static List<Integer> historyFromString(String value) {
        return null;
    }


    @Override
    public void createTask(String name, String description, Status status) {
        super.createTask(name, description, status);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void createSubtask(String name, String description, Status status, Integer epicId) {
        super.createSubtask(name, description, status, epicId);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void createEpic(String name, String description, Status status) {
        super.createEpic(name, description, status);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void calculateEpicStatus(Integer epicId) {
        super.calculateEpicStatus(epicId);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateTask(Integer id, Task task) {
        super.updateTask(id, task);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateSubtask(Integer id, Subtask subtask) {
        super.updateSubtask(id, subtask);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateEpic(Integer id, Epic epic) {
        super.updateEpic(id, epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeTaskById(Integer id) {
        super.removeTaskById(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeEpicById(Integer id) {
        super.removeEpicById(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeSubtaskById(Integer id) {
        super.removeSubtaskById(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "FileBackedTasksManager{" +
                "file=" + file +
                '}';
    }
}
