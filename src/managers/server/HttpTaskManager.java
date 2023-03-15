package managers.server;

import managers.file.FileBackedTasksManager;
import managers.file.ManagerSaveException;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;

public class HttpTaskManager extends FileBackedTasksManager {
    private final URL url;
    public final KVTaskClient kvTaskClient;
    public final HttpTaskServer httpTaskServer;

    public HttpTaskManager(URL url) throws IOException, InterruptedException {
        this.url = url;
        kvTaskClient = new KVTaskClient(url);
        this.httpTaskServer = new HttpTaskServer();
    }

    public void put(String key, String value) throws IOException, InterruptedException {
        kvTaskClient.put(key, value);
    }

    public String load(String key) throws IOException, InterruptedException {
        return kvTaskClient.load(key);
    }
}
