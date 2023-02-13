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

            fw.write("\n");
            int counter = 0;
            for (Task task : getHistory()) { // запись истории
                fw.write(task.getId().toString());

                if (counter < getHistory().size() - 1) {
                    fw.write(",");
                }
                counter++;
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка ввода");
        }
    }

    static void historyToString(Integer id) throws ManagerSaveException {
        try (FileWriter fw = new FileWriter(path.toFile(), StandardCharsets.UTF_8)) {
            //fw.write(manager.getHistory().toString());
            fw.write(id);

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи истории");
        }
        //System.out.println("history" + manager.getHistory());
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
            String history = "";


            int counter = 0;
            for (String line : fileLine) {
                counter++;
                if (isFirstIteration) {
                    isFirstIteration = false;
                    continue;
                }

                if (counter == fileLine.length - 1) {
                    continue;
                }

                if (!(line.isBlank()) && !(counter == fileLine.length)) {
                    fromString(line);
                }

                if (counter == fileLine.length) {
                    history += line;
                    for (String taskId : history.split(",")) {
                        //System.out.println(taskId);
                        //super.getAnyTaskById(Integer.parseInt(taskId));
                        System.out.println(super.getAnyTaskById(Integer.parseInt(taskId)).getId());
                    }
                }
            }

                //historyManager.add(super.getAnyTaskById(Integer.parseInt(taskId)));

            //setEpicSubtasksId();

        } catch (IOException e) {
            throw new ManagerSaveException("Невозможно прочитать файл. Возможно файл не находится в нужной директории.");
        }
    }


    @Override
    public Epic getEpic(Integer id) {
        Epic epic = super.getEpic(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
        return epic;
    }


   /* public Task getAnyTaskById(int id) {
        Task task = null;

        if (tasks.containsKey(id)) {
            task = getTask(id);
        }
        if (subtasks.containsKey(id)) {
            task = getSubtask(id);
        }
        if (epicList.contains(id)) {
            task = getEpic(id);
        }
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }

        return task;
    }*/

    @Override
    public Task getTask(Integer id) {
        Task task = super.getTask(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
        return task;
    }

    @Override
    public Subtask getSubtask(Integer id) {
        Subtask subtask = super.getSubtask(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
        return subtask;
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
        int epicId = -1;

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
