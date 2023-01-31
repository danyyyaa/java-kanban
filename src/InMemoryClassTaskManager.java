import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryClassTaskManager implements TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Subtask> subtasks;
    private HashMap<Integer, Epic> epicsWithoutSubtasks;
    private ArrayList<Epic> epicList;
    private Integer id;

    InMemoryClassTaskManager() {
        id = 0;
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epicsWithoutSubtasks = new HashMap<>();
        epicList = new ArrayList<>();
    }

    @Override
    public void createTask(String name, String description, Status status) {
        Task task = new Task(name, description, status, id);
        tasks.put(id, task);
        id++;
    }

    @Override
    public void createSubtask(String name, String description, Status status, Integer epicId) {
        Subtask subtask = new Subtask(name, description, status, id, epicId);

        subtasks.put(id, subtask);

        for (Epic epic : epicList) {
            if (epic.id.equals(epicId)) {
                epic.getSubtasksId().add(id);
            }
        }
        id++;
    }

    @Override
    public void createEpic(String name, String description, Status status) {
        ArrayList<Integer> subtasksId = new ArrayList<>();
        Epic epic = new Epic(name, description, status, id, subtasksId);
        epicsWithoutSubtasks.put(id, epic);
        id++;
        epicList.add(epic);
    }

    @Override
    public void calculateEpicStatus(Integer epicId) {
        for(Epic epic : epicList){
            if(epic.id.equals(epicId)){

                int counterNew = 0;
                int counterDone = 0;

                for(Integer id : epic.getSubtasksId()){
                    if(subtasks.get(id).getStatus().equals(Status.DONE)){
                        counterDone++;
                    }
                    if(subtasks.get(id).getStatus().equals(Status.NEW)){
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
    public Object getListAllTasks() {
        return tasks.values();
    }

    @Override
    public Object getListAllSubtasks() {
        return subtasks.values();
    }

    @Override
    public Object getListAllEpics() {
        return epicList;
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epicsWithoutSubtasks.clear();
        epicList.clear();
    }

    @Override
    public Task getTask(Integer id) {
        return tasks.get(id);
    }

    @Override
    public Subtask getSubtask(Integer id) {
        return subtasks.get(id);
    }

    @Override
    public Epic getEpic(Integer id) {
        for (Epic epic : epicList) {
            if (epic.getId() == id) {
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
        tasks.remove(id);
    }

    @Override
    public void removeEpicById(Integer id) {
        epicsWithoutSubtasks.remove(id);

        for (Epic epic : epicList) {
            if (epic.getId() == id) {
                epicList.remove(epic);
                break;
            }
        }
    }

    @Override
    public void removeSubtaskById(Integer id) {
        subtasks.remove(id);
        for (int i = 0; i < epicList.size(); i++) {
            for (Integer subtaskId : epicList.get(i).getSubtasksId()){
                if (subtaskId == id) {
                    epicList.get(i).getSubtasksId().remove(i);
                    break;
                }
            }
        }
    }
}
