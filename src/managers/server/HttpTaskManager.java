package managers.server;



import managers.file.FileBackedTasksManager;
import managers.task.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    public final URL url;
    public final KVTaskClient kvTaskClient;
    public final HttpTaskServer httpTaskServer;

    public HttpTaskManager(URL url) throws IOException, InterruptedException {
        this.url = url;
        kvTaskClient = new KVTaskClient(url);
        this.httpTaskServer = new HttpTaskServer();
    }

    public KVTaskClient getKvTaskClient() {
        return kvTaskClient;
    }

    public void stop() {
        httpTaskServer.stop();
    }

    @Override
    public void save() {
        URI tasksUri = URI.create("http://localhost:8078/save/tasks?API_TOKEN=" + kvTaskClient.getApiToken());
        URI epicUri = URI.create("http://localhost:8078/save/epics?API_TOKEN=" + kvTaskClient.getApiToken());
        URI subtasksUri = URI.create("http://localhost:8078/save/subtasks?API_TOKEN=" + kvTaskClient.getApiToken());
        // и для истории
        URI historyUri = URI.create("http://localhost:8078/save/history?API_TOKEN=" + kvTaskClient.getApiToken());

        HttpRequest requestTasks = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("{}")) // тело запроса - все задачи в формате json: "[{"id":1}, {"id":2}]"
                .uri(tasksUri)
                .build();
        // + запросы для эпиков, подзадач и истории
        HttpRequest requestSubtasks = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("{}")) // тело запроса - все задачи в формате json: "[{"id":1}, {"id":2}]"
                .uri(subtasksUri)
                .build();
        HttpRequest requestEpics = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("{}")) // тело запроса - все задачи в формате json: "[{"id":1}, {"id":2}]"
                .uri(epicUri)
                .build();
        HttpRequest requestHistory = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("{}")) // тело запроса - все задачи в формате json: "[{"id":1}, {"id":2}]"
                .uri(historyUri)
                .build();

        try {
            kvTaskClient.getClient().send(requestTasks, HttpResponse.BodyHandlers.ofString());
            kvTaskClient.getClient().send(requestSubtasks, HttpResponse.BodyHandlers.ofString());
            kvTaskClient.getClient().send(requestEpics, HttpResponse.BodyHandlers.ofString());
            kvTaskClient.getClient().send(requestHistory, HttpResponse.BodyHandlers.ofString());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка получения данных с сервера.");
        }
    }

    @Override
    public void loadFromFile() {
        URI tasksUri = URI.create("http://localhost:8078/load/tasks?API_TOKEN=" + kvTaskClient.getApiToken());
        URI epicUri = URI.create("http://localhost:8078/load/epics?API_TOKEN=" + kvTaskClient.getApiToken());
        URI subtasksUri = URI.create("http://localhost:8078/load/subtasks?API_TOKEN=" + kvTaskClient.getApiToken());

        // делаем запросы к kvserver, получаем от него задачи в формате json и складываем их в хэш мапы

    }

}



