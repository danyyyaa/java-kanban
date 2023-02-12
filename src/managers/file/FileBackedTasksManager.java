package managers.file;

import managers.history.HistoryManager;
import managers.task.InMemoryClassTaskManager;
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
    static Path path;

    public FileBackedTasksManager(Path path) throws ManagerSaveException {
        this.path = path;
        loadFromFile(path);
    }

    public void save() throws ManagerSaveException {
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка создания/чтения файла");
        }

        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (FileWriter fw = new FileWriter(path.toFile(), StandardCharsets.UTF_8)) {
            fw.write("id,type,name,status,description,epic\n");
            for (Task task : getListAllTasks()) {
                fw.write(toString(task));
            }
            for (Subtask task : getListAllSubtasks()) {
                fw.write(toString(task));
            }
            for (Epic task : getListAllEpics()) {
                fw.write(toString(task));
            }
            //fw.write(historyToString(historyManager));
            System.out.println("history manager: " + getHistory());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка ввода");
        }
    }


    //(BufferedReader fileReader = new BufferedReader(new FileReader(file.getAbsolutePath(), StandardCharsets.UTF_8)))
    public void loadFromFile(Path path) throws ManagerSaveException {
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка создания/чтения файла");
        }

        try {
            String fileData = Files.readString(Path.of(path.toFile().getAbsolutePath()), StandardCharsets.UTF_8);
            String[] fileLine = fileData.split("\\r?\\n");

            boolean isFirstIteration = true;

            for (String line : fileLine) {
                if (isFirstIteration) {
                    isFirstIteration = false;
                    continue;
                }
                fromString(line);

            }
            setEpicSubtasksId();

        } catch (IOException e) {
            throw new ManagerSaveException("Невозможно прочитать файл. Возможно файл не находится в нужной директории.");
        }
    }

    static void historyToString(HistoryManager manager) {

        System.out.println("history" + manager.getHistory());


    }

    static List<Integer> historyFromString(String value) {
        return null;
    }

    void setEpicSubtasksId() {
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() != null) {
                for (Epic epic : epicList) {
                    if (epic.getId().equals(subtask.getEpicId())
                            && epic.getSubtasksId().contains(subtask.getEpicId())) {
                        epic.addSubtaskId(subtask.getEpicId());
                    }
                }
            }
        }
    }

    String toString(Task task) {
        String result = task.getId() + ",";

        if (task instanceof Subtask) {
            result += TasksType.SUBTASK + ",";
        } else if (task instanceof Epic) {
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

    void fromString(String value) {
        String[] data = value.split(",");

        Integer id = Integer.parseInt(data[0]);
        String taskType = data[1];
        String name = data[2];
        Status status = Status.valueOf(data[3]);
        String description = data[4];
        Integer epicId = -1;

        if (taskType.equals(TasksType.SUBTASK.toString())) {
            epicId = Integer.parseInt(data[5]);
        }

        switch (taskType) {
            case "TASK":
                tasks.put(id, new Task(name, description, status, id));
                break;
            case "SUBTASK":
                subtasks.put(id, new Subtask(name, description, status, id, epicId));
                break;
            case "EPIC":
                epicList.add(new Epic(name, description, status, id));
                break;
        }
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
                "path=" + path +
                '}';
    }
}
