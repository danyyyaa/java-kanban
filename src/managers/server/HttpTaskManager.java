package managers.server;



import com.google.gson.*;
import com.google.gson.internal.bind.util.ISO8601Utils;
import managers.file.FileBackedTasksManager;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HttpTaskManager extends FileBackedTasksManager {
    public final URL url;
    KVServer kvServer;
    public final KVTaskClient kvTaskClient;
    public final HttpTaskServer httpTaskServer;
    private final Gson gson;

    public HttpTaskManager(URL url) throws IOException, InterruptedException {
        this.url = url;
        gson = new Gson();
        kvServer = new KVServer();
        this.httpTaskServer = new HttpTaskServer();
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
            List<Task> tasksJson = StringToJsonFormat(kvTaskClient.load("tasks"));
            List<Task> subtasksJson = StringToJsonFormat(kvTaskClient.load("subtasks"));
            List<Task> epicsJson = StringToJsonFormat(kvTaskClient.load("epics"));

            for (Task task : tasksJson) {
                tasks.put(task.getId(), task);
            }

            for (Task task : subtasksJson) {
                tasks.put(task.getId(), task);
            }

            for (Task task : epicsJson) {
                tasks.put(task.getId(), task);
            }


            /*String responseTasksJson = kvTaskClient.load("tasks");
            String responseSubtasksJson = kvTaskClient.load("subtasks");
            String responseEpicsJson = kvTaskClient.load("epics");
            String responseHistoryJson = kvTaskClient.load("history");

            responseTasksJson = responseTasksJson.substring(2, responseTasksJson.length() - 2);
            StringBuilder tasksStringJson = new StringBuilder();

            for (String s : responseTasksJson.split("\\\\")) {
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
                Task task = gson.fromJson(splitTasksJson[i], Task.class);
                tasks.put(task.getId(), task);
            }*/

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



