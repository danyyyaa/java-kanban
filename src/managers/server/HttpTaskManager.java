package managers.server;

import com.google.gson.*;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    HttpClient client = HttpClient.newHttpClient();
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
        setHttpTaskServer(httpTaskServer);
        setKvTaskClient(kvTaskClient);
    }

    @Override
    public void createTask(String name, String description, Status status, String startTime, String duration) {
        super.createTask(name, description, status, startTime, duration);
    }

    @Override
    public List<Task> getListAllTasks() {
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/task");

        HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).GET().build();


        try {
            String response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString()).body();
            System.out.println("response " + response);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        // System.out.println("values " + tasks.values());
        return null;
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



