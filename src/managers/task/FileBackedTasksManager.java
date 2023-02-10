package managers.task;

import managers.history.HistoryManager;
import managers.util.Managers;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileBackedTasksManager extends InMemoryClassTaskManager {
    File file;
    static TaskManager taskManager = Managers.getDefault();
    static HistoryManager historyManager = Managers.getDefaultHistory();

    public FileBackedTasksManager (File file) {
        this.file = file;
    }

    public static void main(String[] args) {
        taskManager.createEpic("Эпик 1", "Описание", Status.NEW); // id = 0
        taskManager.createSubtask("Подзадача 1", "Описание", Status.NEW, 0); // id = 1
        taskManager.createSubtask("Подзадача 2", "Описание", Status.NEW, 0); // id = 2
        taskManager.createSubtask("Подзадача 3", "Описание", Status.NEW, 0); // id = 3

        taskManager.createEpic("Эпик 2", "Описание", Status.NEW); // id = 4
        taskManager.createTask("Задача 1", "Описание", Status.IN_PROGRESS); // id = 5

        historyManager.add(taskManager.getSubtask(3));
        historyManager.add(taskManager.getEpic(0));
        historyManager.add(taskManager.getTask(5));

        System.out.println(historyManager.getHistory());


    }

    public void save() throws ManagerSaveException {
        try (FileWriter fw = new FileWriter(file)) {
            fw.write("fsdgsdfg");
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка");
        }
    }

    static FileBackedTasksManager loadFromFile(File file) {
        return null;
    }

    static String historyToString(HistoryManager manager) {
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
    public Object getListAllTasks() {
        return super.getListAllTasks();
        //save();
    }

    @Override
    public Object getListAllSubtasks() {
        return super.getListAllSubtasks();
        //save();
    }

    @Override
    public Object getListAllEpics() {
        return super.getListAllEpics();
        //save();
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
    public Task getTask(Integer id) {
        return super.getTask(id);
        //save();
    }

    @Override
    public Subtask getSubtask(Integer id) {
        return super.getSubtask(id);
        //save();
    }

    @Override
    public Epic getEpic(Integer id) {
        return super.getEpic(id);
        //save();
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
