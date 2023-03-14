import managers.file.FileBackedTasksManager;
import managers.file.ManagerSaveException;
import managers.server.HttpTaskServer;
import managers.server.KVServer;
import managers.server.KVTaskClient;
import managers.task.TaskManager;
import managers.util.Managers;
import tasks.Status;
import tasks.Task;
import java.io.IOException;


import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        new KVServer().start();
        KVTaskClient client = new KVTaskClient(new URL("http://localhost:" + KVServer.PORT));
        client.load("qwe");

    }

    private static void createTasks() {

    }
}