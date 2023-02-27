package managers.task;

import managers.history.HistoryManager;
import managers.util.Managers;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class InMemoryTaskManager implements TaskManager {
    final protected HashMap<Integer, Task> tasks;
    final protected HashMap<Integer, Subtask> subtasks;
    final private HashMap<Integer, Epic> epicsWithoutSubtasks;
    final protected ArrayList<Epic> epicList;
    private static Integer id;
    public HistoryManager historyManager;

    public InMemoryTaskManager() {
        id = -1;
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epicsWithoutSubtasks = new HashMap<>();
        epicList = new ArrayList<>();
        historyManager = Managers.getDefaultHistory();
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void createTask(String name, String description, Status status) {
        id = idGenerator();
        Task task = new Task(name, description, status, id);
        tasks.put(id, task);
    }

    private int idGenerator() {
        int id = this.id;
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
    public void createSubtask(String name, String description, Status status, Integer epicId) {
        id = idGenerator();

        Subtask subtask = new Subtask(name, description, status, id, epicId);

        subtasks.put(id, subtask);

        for (Epic epic : epicList) {
            if (epic.getId().equals(epicId)) {
                epic.getSubtasksId().add(id);
            }
        }
    }

    @Override
    public void createEpic(String name, String description, Status status) {
        ArrayList<Integer> subtasksId = new ArrayList<>();
        id = idGenerator();

        Epic epic = new Epic(name, description, status, id, subtasksId);
        epicsWithoutSubtasks.put(id, epic);
        epicList.add(epic);
    }

    @Override
    public void calculateEpicStatus(Integer epicId) {
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
        for (int task : tasks.keySet()) {
            historyManager.remove(task);
        }
        tasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (int subtask : subtasks.keySet()) {
            historyManager.remove(subtask);
        }

        subtasks.clear();

        for (Epic epic : epicList) {
            epic.getSubtasksId().clear();
            calculateEpicStatus(epic.getId());
        }
    }

    @Override
    public void deleteAllEpics() {
        for (Epic epic : epicList) {
            historyManager.remove(epic.getId());
        }
        for (int subtask : subtasks.keySet()) {
            historyManager.remove(subtask);
        }

        epicsWithoutSubtasks.clear();
        epicList.clear();
    }

    @Override
    public Task getTask(Integer id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Subtask getSubtask(Integer id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Epic getEpic(Integer id) {
        for (Epic epic : epicList) {
            if (Objects.equals(epic.getId(), id)) {
                historyManager.add(epic);
                return epic;
            }
        }
        return null;
    }

    @Override
    public void updateTask(Integer id, Task task) {
        tasks.put(id, task);
    }

    @Override
    public void updateSubtask(Integer id, Subtask subtask) {
        subtasks.put(id, subtask);
    }

    @Override
    public void updateEpic(Integer id, Epic epic) {
        epicsWithoutSubtasks.put(id, epic);

        for (int i = 0; i < epicList.size(); i++) {
            if (epicList.get(i).getId() == id) {
                epicList.set(i, epic);
            }
        }
    }

    @Override
    public void removeTaskById(Integer id) {
        historyManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public void removeEpicById(Integer id) {
        epicsWithoutSubtasks.remove(id);

        for (Epic epic : epicList) {
            if (epic.getId() == id) {
                if (historyManager.getHistory().contains(id)) {
                    historyManager.remove(id);
                }
                for (Integer subtaskId : epic.getSubtasksId()) {
                    subtasks.remove(subtaskId);
                }
                epicList.remove(epic);
                break;
            }
        }
    }

    @Override
    public void removeSubtaskById(Integer id) {
        if (this.subtasks.containsKey(id)) {
            subtasks.remove(id);
            historyManager.remove(id);

            for (int i = 0; i < epicList.size(); i++) {
                for (Integer subtaskId : epicList.get(i).getSubtasksId()) {
                    if (Objects.equals(subtaskId, id)) {
                        epicList.get(i).getSubtasksId().remove(subtaskId);
                        break;
                    }
                }
            }
        }
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public ArrayList<Epic> getEpicList() {
        return epicList;
    }
}
