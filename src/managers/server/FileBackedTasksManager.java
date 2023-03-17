package managers.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import managers.file.ManagerSaveException;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.System.exit;

public class FileBackedTasksManager extends managers.file.FileBackedTasksManager {
    private final Gson gson;
    private KVTaskClient kvTaskClient;
    private HttpTaskServer httpTaskServer;

    public FileBackedTasksManager() {
        //gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter()).create();
        gson = new Gson();
    }

    @Override
    public void save() {
        try {
            /*kvTaskClient.put("tasks", gson.toJson(getListAllTasks()));
            kvTaskClient.put("subtasks", gson.toJson(getListAllSubtasks()));
            kvTaskClient.put("epics", gson.toJson(getListAllEpics()));
            kvTaskClient.put("history", gson.toJson(getHistory()));*/

            kvTaskClient.put("tasks", gson.toJson(tasks.values()));
            kvTaskClient.put("subtasks", gson.toJson(subtasks.values()));
            kvTaskClient.put("epics", gson.toJson(epics.values()));
            kvTaskClient.put("history", gson.toJson(getHistory()));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void load() {
        try {
            for (Task task : Objects.requireNonNull(StringToJsonFormat(kvTaskClient.load("tasks")))) {
                tasks.put(task.getId(), task);
            }

            for (Task task : Objects.requireNonNull(StringToJsonFormat(kvTaskClient.load("subtasks")))) {
                if (task != null) {
                    subtasks.put(task.getId(), (Subtask) task);
                }
            }

            for (Task task : Objects.requireNonNull(StringToJsonFormat(kvTaskClient.load("epics")))) {
                if (task != null) {
                    epics.put(task.getId(), (Epic) task);
                }
            }

            for (Task task : Objects.requireNonNull(StringToJsonFormat(kvTaskClient.load("history")))) {
                if (task != null) {
                    historyManager.add(task);
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Task> StringToJsonFormat(String string) {
        if (!string.isBlank()) {
            List<Task> tasks = new ArrayList<>();
            string = string.substring(2, string.length() - 2);
            StringBuilder tasksStringJson = new StringBuilder();

            for (String s : string.split("\\\\")) {
                tasksStringJson.append(s);
            }

            String[] splitTasksJson = tasksStringJson.toString().split("},\\{");

            for (int i = 0; i < splitTasksJson.length; i++) {
                if (i != splitTasksJson.length - 1) {
                    splitTasksJson[i] += "}";
                }
                if (i != 0) {
                    splitTasksJson[i] = "{" + splitTasksJson[i];
                }
                //System.out.println(splitTasksJson[i]);
                tasks.add(gson.fromJson(splitTasksJson[i], Task.class));
            }
            return tasks;
        }
        return null;
    }


    public void setKvTaskClient(KVTaskClient kvTaskClient) {
        this.kvTaskClient = kvTaskClient;
    }

    public void setHttpTaskServer(HttpTaskServer httpTaskServer) {
        this.httpTaskServer = httpTaskServer;
    }


}

