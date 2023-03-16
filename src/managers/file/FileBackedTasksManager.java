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
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static managers.file.TasksType.*;
import static tasks.Status.NEW;

public class FileBackedTasksManager extends InMemoryTaskManager {
    static Path path;

    public FileBackedTasksManager(Path path) throws ManagerSaveException {
        FileBackedTasksManager.path = path;
        load(path);
    }

    public FileBackedTasksManager() {}


    public void save() throws ManagerSaveException {
        try (PrintWriter pw = new PrintWriter(path.toFile())) {
            pw.write("id,type,name,status,description,startTime,duration,endTime,epic\n");
            for (Task task : tasks.values()) {
                pw.write(toString(task));
            }
            for (Subtask subtask : subtasks.values()) {
                pw.write(toString(subtask));
            }
            for (Epic epic : epics.values()) {
                pw.write(toString(epic));
            }

            pw.write("\n");
            int counter = 0;
            for (Task task : historyManager.getHistory()) {
                pw.write(task.getId().toString());

                if (counter < historyManager.getHistory().size() - 1) {
                    pw.write(",");
                }
                counter++;
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка ввода");
        }
    }

    @Override
    public void calculateEpicTime(int epicId) {
        super.calculateEpicTime(epicId);
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

    public void load() {};

    public void load(Path path) throws ManagerSaveException {
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
            boolean isBlankLine = true;

            int counter = 0;
            for (String line : fileLine) {

                counter++;

                if (isFirstIteration) {
                    isFirstIteration = false;
                    continue;
                }
                if (line.isBlank()) {
                    isBlankLine = false;
                    continue;
                }

                if (isBlankLine) {
                    fromString(line);
                }

                if (counter == fileLine.length && !isBlankLine) {
                    readHistory(line);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Невозможно прочитать файл. Возможно файл не находится в нужной директории.");
        }
        setEpicSubtasksId();
    }

    private void readHistory(String line) {
        for (String taskId : line.split(",")) {
            if (getTasks().containsKey(Integer.parseInt(taskId))) {
                historyManager.add(tasks.get(Integer.parseInt(taskId)));
            }
            if (getSubtasks().containsKey(Integer.parseInt(taskId))) {
                historyManager.add(subtasks.get(Integer.parseInt(taskId)));
            }
            if (getEpics().containsKey((Integer.parseInt(taskId)))) {
                historyManager.add(epics.get(Integer.parseInt(taskId)));
            }
        }
    }

    private String toString(Task task) {
        String result = task.getId() + ",";

        if (task instanceof Subtask) {
            result += TasksType.SUBTASK + ",";
        } else if (task instanceof Epic) {
            result += EPIC + ",";
        } else {
            result += TASK + ",";
        }

        result += task.getName() + ",";
        result += task.getStatus() + ",";
        result += task.getDescription() + ",";
        result += task.getStartTime() + ",";
        result += task.getDuration() + ",";
        result += task.getEndTime() + ",";

        if (task instanceof Subtask) {
            result += ((Subtask) task).getEpicId() + ",";
        }

        return result + "\n";
    }

    private void fromString(String value) {
        String[] data = value.split(",");

        Integer id = Integer.parseInt(data[0]);
        TasksType taskType = TasksType.valueOf(data[1]);
        String name = data[2];
        Status status;

        try {
            status = Status.valueOf(data[3]);
        } catch (IllegalArgumentException e) {
            status = NEW;
        }

        String description = data[4];
        String startTime = LocalDateTime.parse(data[5]).format(DATE_TIME_FORMATTER);

        String duration = data[6].substring(2, data[6].length() - 1);


        int epicId = -1;

        if (SUBTASK.equals(taskType)) {
            epicId = Integer.parseInt(data[8]);
        }

        switch (taskType) {
            case TASK:
                tasks.put(id, new Task(name, description, status, id, startTime, duration));
                break;
            case SUBTASK:
                subtasks.put(id, new Subtask(name, description, status, id, epicId, startTime, duration));
                break;
            case EPIC:
                epics.put(id, new Epic(name, description, status, id, startTime, duration));
                break;
        }

        if (EPIC.equals(taskType)) {
            try {
                String endTime = LocalDateTime.parse(data[7]).format(DATE_TIME_FORMATTER);
                epics.get(id).setEndTime(LocalDateTime.parse(endTime, DATE_TIME_FORMATTER));
            } catch (DateTimeParseException e) {
                epics.get(id).setEndTime(LocalDateTime.parse("11:11, 11.11.50", DATE_TIME_FORMATTER));
            }
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

    private void setEpicSubtasksId() {
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() != null) {
                int epicId = subtask.getEpicId();
                epics.get(epicId).addSubtaskId(subtask.getId());
            }
        }
    }

    @Override
    public void createTask(String name, String description, Status status, String startTime, String duration) {
        super.createTask(name, description, status, startTime, duration);
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
}
