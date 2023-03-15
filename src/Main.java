import managers.file.FileBackedTasksManager;
import managers.file.ManagerSaveException;
import managers.server.HttpTaskManager;
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
    public static void main(String[] args) throws IOException, InterruptedException {
        /*new KVServer().start();
        //TaskManager httpManager = Managers.getDefaultHttp(new URL("http://localhost:" + KVServer.PORT));
        HttpTaskManager httpManager = new HttpTaskManager(new URL("http://localhost:" + KVServer.PORT));

        httpManager.put("1", "один");
        String one = httpManager.load("1");
        System.out.println(one);*/

        new HttpTaskServer();


        //new KVServer().start();
        /*KVTaskClient client = new KVTaskClient(new URL("http://localhost:" + KVServer.PORT));

        client.put("1", "11");
        client.put("1", "2");
        client.put("2", "33");
        String response = client.load("1");
        String response1 = client.load("2");

        System.out.println("response: " + response);
        System.out.println("response1: " + response1);*/

        /*HttpTaskManager httpTaskManager = new HttpTaskManager(new URL("http://localhost:" + KVServer.PORT));
        httpTaskManager.kvTaskClient.put("1", "11");
        httpTaskManager.kvTaskClient.put("1", "2");
        httpTaskManager.kvTaskClient.put("2", "33");

        String response = httpTaskManager.kvTaskClient.load("1");
        String response1 = httpTaskManager.kvTaskClient.load("2");



        System.out.println("response: " + response);
        System.out.println("response1: " + response1);*/
    }

    private static void createTasks() {

    }
}