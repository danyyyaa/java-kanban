package managers.file;

import managers.task.InMemoryTaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBackedTasksManager extends InMemoryTaskManager {
    static Path path;

    public FileBackedTasksManager(Path path) throws ManagerSaveException {
        FileBackedTasksManager.path = path;
        loadFromFile(path);
    }

    private void save() throws ManagerSaveException {
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
            fw.write("id,type,name,status,description,startTime,duration,endTime,epic\n");
            for (Task task : getListAllTasks()) {
                fw.write(toString(task));
            }
            for (Subtask subtask : getListAllSubtasks()) {
                fw.write(toString(subtask));
            }
            for (Epic epic : getListAllEpics()) {
                fw.write(toString(epic));
            }

            fw.write("\n");
            int counter = 0;
            for (Task task : getHistory()) {
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

    private void loadFromFile(Path path) throws ManagerSaveException {
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                throw new ManagerSaveException("Ошибка создания/чтения файла");
            }
        }

        try {
            String fileData = Files.readString(Path.of(path.toFile().getAbsolutePath()), StandardCharsets.UTF_8);
            String[] fileLine = fileData.split("\\r?\\n");
            boolean isFirstIteration = true;

            int counter = 0;
            for (String line : fileLine) {
                counter++;

                if ((counter == fileLine.length - 1) || isFirstIteration) {
                    isFirstIteration = false;
                    continue;
                }
                if (!(line.isBlank()) && !(counter == fileLine.length)) {
                    fromString(line);
                }
                if (counter == fileLine.length) {
                    readHistory(line);
                }
            }
            setEpicSubtasksId();
        } catch (IOException e) {
            throw new ManagerSaveException("Невозможно прочитать файл. Возможно файл не находится в нужной директории.");
        }
    }

    private void readHistory(String line) {
        if (getHistory().size() != 0) {
            for (String taskId : line.split(",")) {
                if (getTasks().containsKey(Integer.parseInt(taskId))) {
                    historyManager.add(tasks.get(Integer.parseInt(taskId)));
                }
                if (getSubtasks().containsKey(Integer.parseInt(taskId))) {
                    historyManager.add(subtasks.get(Integer.parseInt(taskId)));
                }
                for (Epic epic : epics.values()) { // изменить
                    if (epic.getId().equals(Integer.valueOf(taskId))) {
                        historyManager.add(epic);
                    }
                }
            }
        }
    }

    private String toString(Task task) {
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

        result += task.getStartTime() + ",";
        result += task.getDuration() + ",";

        if (task instanceof Epic) {
            super.calculateEpicTime(task.getId());
            //System.out.println(super.getEpic(0));
        }

        result += task.getEndTime() + ",";

        if (task instanceof Subtask) {
            result += ((Subtask) task).getEpicId() + ",";
        }

        return result + "\n";
    }

    private void fromString(String value) {
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
                epics.put(id, new Epic(name, description, status, id));
                break;
        }
    }

    @Override
    public Task getAnyTaskById(int id) {
        Task task = super.getAnyTaskById(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
        return epic;
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
        return task;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
        return subtask;
    }

    void setEpicSubtasksId() {
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() != null) {
                for (Epic epic : epics.values()) { // изменить
                    if (epic.getId().equals(subtask.getEpicId())
                            && epic.getSubtasksId().contains(subtask.getEpicId())) {
                        epic.addSubtaskId(subtask.getEpicId());
                    }
                }
            }
        }
    }

    @Override
    public void createTask(String name, String description, Status status, String startDate, String duration) {
        super.createTask(name, description, status, "14:09, 12.07.21", "25");
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void createSubtask(String name, String description, Status status,
                              int epicId, String startTime, String duration) {
        super.createSubtask(name, description, status, epicId, startTime, duration);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void createEpic(String name, String description, Status status, String startDate, String duration) {
        super.createEpic(name, description, status, startDate, duration);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void calculateEpicStatus(int epicId) {
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
    public void updateTask(int id, Task task) {
        super.updateTask(id, task);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateSubtask(int id, Subtask subtask) {
        super.updateSubtask(id, subtask);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateEpic(int id, Epic epic) {
        super.updateEpic(id, epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeAnyTaskById(int id) {
        super.removeAnyTaskById(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeSubtaskById(int id) {
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
