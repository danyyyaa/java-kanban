import managers.server.HttpTaskServer;

import managers.server.KVServer;
import managers.task.TaskManager;
import managers.util.Managers;

import tasks.Status;
import java.io.IOException;
import java.net.URL;

public class Main {
    static TaskManager httpTaskManager;

    public static void main(String[] args) throws IOException, InterruptedException {
        KVServer kvServer = new KVServer();
        kvServer.start();

        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

        httpTaskManager = Managers.getDefault(new URL("http://localhost:" + KVServer.PORT));


        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 12.07.21", "20");
        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 13.07.21", "20");
        System.out.println(httpTaskManager.getListAllTasks());

        kvServer.stop();
        httpTaskServer.stop();
    }
}