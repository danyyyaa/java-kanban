package managers.task;

import managers.history.HistoryManager;
import managers.util.Managers;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileBackedTasksManager extends InMemoryClassTaskManager {
    static File file;

    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }

    public static void main(String[] args) throws ManagerSaveException {
        FileBackedTasksManager fileBackedTasksManager = loadFromFile(new File("123.csv"));

        fileBackedTasksManager.createEpic("Эпик 1", "Description", Status.NEW); // id = 0
        fileBackedTasksManager.createSubtask("Подзадача 1", "Description", Status.NEW, 0); // id = 1
        fileBackedTasksManager.createSubtask("Подзадача 1", "Description", Status.NEW, 0); // id = 2
        fileBackedTasksManager.createSubtask("Подзадача 1", "Description", Status.NEW, 0); // id = 3
        fileBackedTasksManager.createTask("Task1", "Description", Status.IN_PROGRESS); // id = 4
        fileBackedTasksManager.createTask("Task2", "Description", Status.DONE); // id = 5
        fileBackedTasksManager.createTask("Task3", "Description", Status.IN_PROGRESS); // id = 6

        // Error
        //fileBackedTasksManager.removeTaskById(5);
        //fileBackedTasksManager.getHistory();

        fileBackedTasksManager.getTask(4);
        fileBackedTasksManager.getTask(6);
        fileBackedTasksManager.getEpic(0);
        fileBackedTasksManager.getSubtask(1);

        //System.out.println(historyManager.getHistory());
        //System.out.println(fileBackedTasksManager.getHistory());
    }

    public void save() throws ManagerSaveException {
        try (FileWriter fw = new FileWriter(file, StandardCharsets.UTF_8)) {
            fw.write("id,type,name,status,description,epic\n");
            for (Task task : getListAllTasks()) {
                fw.write(toString(task));
            }
            for (Task task : getListAllSubtasks()) {
                fw.write(toString(task));
            }
            for (Task task : getListAllEpics()) {
                fw.write(toString(task));
            }
            System.out.println("history manager : " + this.historyManager.getHistory());
            //fw.write("\n" + historyToString(historyManager));

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка ввода");
        }
    }


    //(BufferedReader fileReader = new BufferedReader(new FileReader(file.getAbsolutePath(), StandardCharsets.UTF_8)))
    static FileBackedTasksManager loadFromFile(File file) throws ManagerSaveException {
        try {
            String fileData = Files.readString(Path.of(file.getAbsolutePath()), StandardCharsets.UTF_8);
            String[] fileLine = fileData.split("\\r?\\n");

            boolean isFirstIteration = true;

            for (String line : fileLine) {
                if(isFirstIteration) {
                    isFirstIteration = false;
                    continue;
                }
                fromString(line);
                //System.out.println(fromString(line));
            }

            return new FileBackedTasksManager(file);
        } catch (IOException e) {
            throw new ManagerSaveException("Невозможно прочитать файл. Возможно файл не находится в нужной директории.");
        }
    }

    String toString(Task task) {
        String result = task.getId() + ",";

        if (task instanceof Subtask) {
            result += TasksType.SUBTASK + ",";
        } else if (task instanceof Epic){
            result += TasksType.EPIC + ",";
        } else {
            result += TasksType.TASK + ",";
        }

        result += task.getName() + ",";
        result += task.getStatus() + ",";
        result += task.getDescription() + ",";

        if (task instanceof Subtask) {
            result += ((Subtask) task).getEpicId() + ",";
        }

        return result + "\n";
    }

    static Task fromString(String value) {
        String[] data = value.split(",");

        Integer id = Integer.parseInt(data[0]);
        String taskType = data[1];
        String name = data[2];
        Status status = Status.valueOf(data[3]);
        String description = data[4];
        Integer epicId = null;

        if (taskType.equals(TasksType.SUBTASK.toString())) {
            epicId = Integer.parseInt(data[5]);
        }

        switch (taskType) {
            case "TASK":
                return new Task(name, description, status, id);
            case "SUBTASK":
                return new Subtask(name, description, status, id, epicId);
            case "EPIC":
                return new Epic(name, description, status, id);
        }

        return null;
    }

    static String historyToString(HistoryManager manager) {
        String history = "";
        /*for (Task task : manager.getHistory()) {
            history += task.getId().toString();
            System.out.println(task.getId());

        }
        System.out.println(history);
*/
        return history;
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
