package managers.task;

import managers.history.HistoryManager;
import managers.util.Managers;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class InMemoryTaskManager implements TaskManager {
    final protected HashMap<Integer, Task> tasks;
    final protected HashMap<Integer, Subtask> subtasks;
    final protected ArrayList<Epic> epicList;
    private static int id;
    public HistoryManager historyManager;
    protected static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm, dd.MM.yy");

    public InMemoryTaskManager() {
        id = -1;
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epicList = new ArrayList<>();
        historyManager = Managers.getDefaultHistory();
    }

    public void calculateEpicTime(int epicId) {
        getEpic(epicId).setEndTime(getEpic(epicId).getStartTime());

        for (Epic epic : epicList) {

            if (epic.getId().equals(epicId)) {

                for (int id : epic.getSubtasksId()) {
                    if (getSubtask(id).getStartTime().isBefore(epic.getStartTime())) {
                        epic.setStartTime(getSubtask(id).getStartTime());
                    }

                    if (getSubtask(id).getStartTime().isAfter(epic.getEndTime())) {
                        epic.setEndTime(getSubtask(id).getEndTime());
                    }
                }
            }
        }
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void createTask(String name, String description, Status status, String startTime, String duration) {
        int id = idGenerator();
        tasks.put(id, new Task(name, description, status, id, startTime, duration));
    }

    private int idGenerator() {
        int id = InMemoryTaskManager.id;
        for (int taskId : tasks.keySet()) {
            if (taskId > id) {
                id = taskId;
            }
        }
        for (int subtaskId : subtasks.keySet()) {
            if (subtaskId > id) {
                id = subtaskId;
            }
        }
        for (Epic epic : epicList) {
            if (epic.getId() > id) {
                id = epic.getId();
            }
        }
        return ++id;
    }

    public Task getAnyTaskById(int id) {
        if (tasks.containsKey(id)) {
            return getTask(id);
        } else if (subtasks.containsKey(id)) {
            return getSubtask(id);
        }
        return getEpic(id);
    }

    @Override
    public void createSubtask(String name, String description, Status status,
                              int epicId, String startTime, String duration) {
        id = idGenerator();

        for (Epic epic : epicList) {
            if (epic.getId().equals(epicId)) {
                epic.getSubtasksId().add(id);
                subtasks.put(id, new Subtask(name, description, status, id, epicId, startTime, duration));
            }
        }
    }

    @Override
    public void createEpic(String name, String description, Status status, String startTime, String duration) {
        id = idGenerator();

        epicList.add(new Epic(name, description, status, id, new ArrayList<>(), startTime, duration));
    }

    @Override
    public void calculateEpicStatus(int epicId) {
        for (Epic epic : epicList) {
            if (epic.getId().equals(epicId)) {

                int counterNew = 0;
                int counterDone = 0;

                for (Integer id : epic.getSubtasksId()) {
                    if (subtasks.get(id).getStatus().equals(Status.DONE)) {
                        counterDone++;
                    }
                    if (subtasks.get(id).getStatus().equals(Status.NEW)) {
                        counterNew++;
                    }
                }

                if (counterNew == epic.getSubtasksId().size() || epic.getSubtasksId().isEmpty()) {
                    epic.setStatus(Status.NEW);
                } else if (counterDone == epic.getSubtasksId().size()) {
                    epic.setStatus(Status.DONE);
                } else {
                    epic.setStatus(Status.IN_PROGRESS);
                }
            }
        }
    }

    @Override
    public List<Task> getListAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Subtask> getListAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Epic> getListAllEpics() {
        return epicList;
    }

    @Override
    public void deleteAllTasks() {
        if (!tasks.isEmpty()) {
            for (int task : tasks.keySet()) {
                historyManager.remove(task);
            }
            tasks.clear();
        }
    }

    @Override
    public void deleteAllSubtasks() {
        if (!subtasks.isEmpty()) {
            for (int subtask : subtasks.keySet()) {
                historyManager.remove(subtask);
            }

            subtasks.clear();

            for (Epic epic : epicList) {
                epic.getSubtasksId().clear();
                calculateEpicStatus(epic.getId());
            }
        }
    }

    @Override
    public void deleteAllEpics() {
        if (!epicList.isEmpty()) {
            for (Epic epic : epicList) {
                historyManager.remove(epic.getId());
            }

            List<Integer> del = new ArrayList<>(subtasks.keySet());
            for(int id : del) {
                subtasks.remove(id);
                historyManager.remove(id);
            }

            epicList.clear();
        }
    }

    @Override
    public Task getTask(Integer id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        for (Epic epic : epicList) {
            if (Objects.equals(epic.getId(), id)) {
                historyManager.add(epic);
                return epic;
            }
        }
        return null;
    }

    @Override
    public void updateTask(int id, Task task) {
        if (tasks.containsKey(id)) {
            tasks.put(id, task);
        }
    }

    @Override
    public void updateSubtask(int id, Subtask subtask) {
        if (subtasks.containsKey(id)) {
            subtasks.put(id, subtask);
        }
    }

    @Override
    public void updateEpic(int id, Epic epic) {

        for (int i = 0; i < epicList.size(); i++) {
            if (Objects.equals(epicList.get(i).getId(), id)) {
                epicList.set(i, epic);
            }
        }
    }

    @Override
    public void removeTaskById(int id) {
        if (tasks.containsKey(id)) {
            historyManager.remove(id);
            tasks.remove(id);
        }
    }

    @Override
    public void removeEpicById(int id) {
        for (Epic epic : epicList) {
            if (Objects.equals(epic.getId(), id)) {
                if (historyManager.getHistory().contains(id)) {
                    historyManager.remove(id);
                }
                for (int subtaskId : epic.getSubtasksId()) {
                    subtasks.remove(subtaskId);
                }
                epicList.remove(epic);
                break;
            }
        }
    }

    @Override
    public void removeSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            subtasks.remove(id);
            historyManager.remove(id);

            for (Epic epic : epicList) {
                for (Integer subtaskId : epic.getSubtasksId()) {
                    if (Objects.equals(subtaskId, id)) {
                        epic.getSubtasksId().remove(subtaskId);
                        break;
                    }
                }
                calculateEpicStatus(epic.getId());
            }
        }
    }

    protected HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    protected HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    protected ArrayList<Epic> getEpicList() {
        return epicList;
    }
}
