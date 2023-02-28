package managers.task;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public interface TaskManager {
    void createTask(String name, String description, Status status);
    void createSubtask(String name, String description, Status status, int epicId);
    void createEpic(String name, String description, Status status);
    void calculateEpicStatus(int epicId);
    List<Task> getListAllTasks();
    List<Subtask> getListAllSubtasks();
    List<Epic> getListAllEpics();
    void deleteAllTasks();
    void deleteAllSubtasks();
    void deleteAllEpics();
    Task getTask(Integer id);
    Subtask getSubtask(int id);
    Epic getEpic(int id);
    void updateTask(int id, Task task);
    void updateSubtask(int id, Subtask subtask);
    void updateEpic(int id, Epic epic);
    void removeTaskById(int id);
    void removeEpicById(int id);
    void removeSubtaskById(int id);
    List<Task> getHistory();
    Task getAnyTaskById(int id);
}
