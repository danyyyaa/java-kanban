package managers.task;

import managers.history.HistoryManager;
import managers.util.Managers;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.time.format.DateTimeFormatter;
import java.util.*;

class DateComparator implements Comparator<Task> {

    @Override
    public int compare(Task o1, Task o2) {
        if (o1.getStartTime().plus(o1.getDuration()).isAfter(o2.getStartTime().plus(o1.getDuration()))) {
            return 1;
        } else {
            return -1;
        }
    }
}

public class InMemoryTaskManager implements TaskManager {
    final protected Map<Integer, Task> tasks;
    final protected Map<Integer, Subtask> subtasks;
    final protected Map<Integer, Epic> epics;
    private static int id;
    public HistoryManager historyManager;
    protected static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm, dd.MM.yy");
    public Map<LocalDateTime, LocalDateTime> timeLocalDateTimeMap;

    public InMemoryTaskManager() {
        id = -1;
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
        timeLocalDateTimeMap = new HashMap<>();
    }

    private void timeValidation(String startTime, String duration, int id) {
        Duration formattedDuration = Duration.ofMinutes(Long.parseLong(duration));
        LocalDateTime startCurrentTask = LocalDateTime.parse(startTime, DATE_TIME_FORMATTER);
        LocalDateTime endCurrentTask = LocalDateTime.parse(startTime, DATE_TIME_FORMATTER).plus(formattedDuration);

        for (Map.Entry<LocalDateTime, LocalDateTime> map : timeLocalDateTimeMap.entrySet()) {
            LocalDateTime startOtherTask = map.getKey();
            LocalDateTime endOtherTask = map.getValue();

            boolean condition1 = startOtherTask.isBefore(startCurrentTask) && startOtherTask.isBefore(endCurrentTask)
                    && endOtherTask.isAfter(startCurrentTask) && endOtherTask.isAfter(endCurrentTask);
            boolean condition2 = startOtherTask.isAfter(startCurrentTask) && startOtherTask.isBefore(endCurrentTask)
                    && endOtherTask.isAfter(endCurrentTask) && endOtherTask.isAfter(startCurrentTask);
            boolean condition3 = startOtherTask.isBefore(startCurrentTask) && startOtherTask.isBefore(endCurrentTask)
                    && endOtherTask.isAfter(startCurrentTask) && endOtherTask.isBefore(endCurrentTask);
            boolean condition4 = startOtherTask.isAfter(startCurrentTask) && startOtherTask.isBefore(endCurrentTask)
                    && endOtherTask.isAfter(startCurrentTask) && endOtherTask.isBefore(endCurrentTask);

            if (condition1 || condition2 || condition3 || condition4) {
                removeAnyTaskById(id);
                throw new RuntimeException("Ошибка, пересечение задач по времени.");
            }
        }
        timeLocalDateTimeMap.put(startCurrentTask, endCurrentTask);
    }

    public void removeAnyTaskById(int id) {
        if (tasks.containsKey(id)) {
            removeTaskById(id);
        }
        if (subtasks.containsKey(id)) {
            removeSubtaskById(id);
        }
        if (epics.containsKey(id)) {
            removeEpicById(id);
        }
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        Set<Task> treeSet = new TreeSet<>(new DateComparator());
        treeSet.addAll(getListAllTypeTasks());

        return new ArrayList<>(treeSet);
    }


    @Override
    public void calculateEpicTime(int epicId) {
        epics.get(epicId).setEndTime(epics.get(epicId).getStartTime());

        if (epics.containsKey(epicId)) {
            for (int id : epics.get(epicId).getSubtasksId()) {
                if (getSubtask(id).getStartTime().isBefore(epics.get(epicId).getStartTime())) {
                    epics.get(epicId).setStartTime(getSubtask(id).getStartTime());
                }

                if (getSubtask(id).getStartTime().isAfter(epics.get(epicId).getEndTime())) {
                    epics.get(epicId).setEndTime(getSubtask(id).getEndTime());
                }
            }
        }
    }

    protected List<Task> getListAllTypeTasks() {
        List<Task> tasksAllType = new ArrayList<>();

        tasksAllType.addAll(tasks.values());
        tasksAllType.addAll(subtasks.values());
        tasksAllType.addAll(epics.values());

        return tasksAllType;
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
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

        for (int epicId : epics.keySet()) {
            if (epicId > id) {
                id = epicId;
            }
        }
        return ++id;
    }

    @Override
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

        if (epics.containsKey(epicId)) {
            epics.get(epicId).getSubtasksId().add(id);
            subtasks.put(id, new Subtask(name, description, status, id, epicId, startTime, duration));
        }
        timeValidation(startTime, duration, id);
    }

    @Override
    public void createTask(String name, String description, Status status, String startTime, String duration) {
        int id = idGenerator();
        tasks.put(id, new Task(name, description, status, id, startTime, duration));
        timeValidation(startTime, duration, id);
    }

    @Override
    public void createEpic(String name, String description, Status status, String startTime, String duration) {
        id = idGenerator();

        epics.put(id, new Epic(name, description, status, id, new ArrayList<>(), startTime, duration));
    }

    @Override
    public void calculateEpicStatus(int epicId) {
        int counterNew = 0;
        int counterDone = 0;

        for (Integer id : epics.get(epicId).getSubtasksId()) {
            if (subtasks.get(id).getStatus().equals(Status.DONE)) {
                counterDone++;
            }
            if (subtasks.get(id).getStatus().equals(Status.NEW)) {
                counterNew++;
            }
        }

        if (counterNew == epics.get(epicId).getSubtasksId().size() || epics.get(epicId).getSubtasksId().isEmpty()) {
            epics.get(epicId).setStatus(Status.NEW);
        } else if (counterDone == epics.get(epicId).getSubtasksId().size()) {
            epics.get(epicId).setStatus(Status.DONE);
        } else {
            epics.get(epicId).setStatus(Status.IN_PROGRESS);
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
        return new ArrayList<>(epics.values());
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

            for (Epic epic : epics.values()) {
                epic.getSubtasksId().clear();
                calculateEpicStatus(epic.getId());
            }
        }
    }

    @Override
    public void deleteAllEpics() {
        if (!epics.isEmpty()) {
            for (Epic epic : epics.values()) {
                historyManager.remove(epic.getId());
            }

            List<Integer> del = new ArrayList<>(subtasks.keySet());
            for (int id : del) {
                subtasks.remove(id);
                historyManager.remove(id);
            }

            epics.clear();
        }
    }

    @Override
    public Task getTask(int id) {
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
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void updateTask(int id, Task task) {
        if (tasks.containsKey(id)) {
            tasks.put(id, task);
            timeValidation(task.getStartTime().format(DATE_TIME_FORMATTER),
                    String.valueOf(task.getDuration().getSeconds() / 60), id);
        }
    }

    @Override
    public void updateSubtask(int id, Subtask subtask) {
        if (subtasks.containsKey(id)) {
            subtasks.put(id, subtask);
            timeValidation(subtask.getStartTime().format(DATE_TIME_FORMATTER),
                    String.valueOf(subtask.getDuration().getSeconds() / 60), id);
        }
    }

    @Override
    public void updateEpic(int id, Epic epic) {
        if (epics.containsKey(id)) {
            epics.put(id, epic);
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
        if (epics.containsKey(id)) {
            for (int subtaskId : epics.get(id).getSubtasksId()) {
                subtasks.remove(subtaskId);
            }

            historyManager.remove(id);
            epics.remove(id);
        }
    }

    @Override
    public void removeSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            subtasks.remove(id);
            historyManager.remove(id);

            int epicId = -7;
            for (Integer subtaskId : subtasks.keySet()) {
                if (Objects.equals(subtaskId, id)) {
                    epicId = subtasks.get(id).getEpicId();
                    epics.get(id).getSubtasksId().remove(subtaskId);
                    break;
                }
            }

            if (epicId != -7) {
                calculateEpicStatus(epics.get(epicId).getId());
            }
        }
    }

    protected Map<Integer, Task> getTasks() {
        return tasks;
    }

    protected Map<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    protected Map<Integer, Epic> getEpics() {
        return epics;
    }
}
