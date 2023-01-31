import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

public interface TaskManager {
    void createTask(String name, String description, Status status);
    void createSubtask(String name, String description, Status status, Integer epicId);
    void createEpic(String name, String description, Status status);
    void calculateEpicStatus(Integer epicId);
    Object getListAllTasks();
    Object getListAllSubtasks();
    Object getListAllEpics();
    void deleteAllTasks();
    void deleteAllSubtasks();
    void deleteAllEpics();
    Task getTask(Integer id);
    Subtask getSubtask(Integer id);
    Epic getEpic(Integer id);
    void updateTask(Integer id, Task task);
    void updateSubtask(Integer id, Subtask subtask);
    void updateEpic(Integer id, Epic epic);
    void removeTaskById(Integer id);
    void removeEpicById(Integer id);
    void removeSubtaskById(Integer id);
}
