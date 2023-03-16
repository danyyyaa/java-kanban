package managers.server;

import com.google.gson.*;
import managers.file.FileBackedTasksManager;
import tasks.Task;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    public final URL url;
    private final KVServer kvServer;
    public final KVTaskClient kvTaskClient;
    public final HttpTaskServer httpTaskServer;
    private final Gson gson;

    public HttpTaskManager(URL url) throws IOException, InterruptedException {
        this.url = url;
        gson = new Gson();
        kvServer = new KVServer();
        httpTaskServer = new HttpTaskServer();
        startServers();
        kvTaskClient = new KVTaskClient(url);
    }

    @Override
    public void save() {
        try {
            kvTaskClient.put("tasks", gson.toJson(getListAllTasks()));
            kvTaskClient.put("subtasks", gson.toJson(getListAllSubtasks()));
            kvTaskClient.put("epics", gson.toJson(getListAllEpics()));
            kvTaskClient.put("history", gson.toJson(getHistory()));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void load() {
        try {
            for (Task task : StringToJsonFormat(kvTaskClient.load("tasks"))) {
                tasks.put(task.getId(), task);
            }

            for (Task task : StringToJsonFormat(kvTaskClient.load("subtasks"))) {
                tasks.put(task.getId(), task);
            }

            for (Task task : StringToJsonFormat(kvTaskClient.load("epics"))) {
                tasks.put(task.getId(), task);
            }

            for (Task task : StringToJsonFormat(kvTaskClient.load("history"))) {
                historyManager.add(task);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Task> StringToJsonFormat(String string) {
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
            tasks.add(gson.fromJson(splitTasksJson[i], Task.class));
        }
        return tasks;
    }

    public void stop() {
        httpTaskServer.stop();
        kvServer.stop();
    }

    public void startServers() {
        kvServer.start();
        httpTaskServer.start();
    }
}



