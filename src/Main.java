import managers.server.HttpTaskManager;
import managers.server.KVServer;
import managers.util.Managers;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        HttpTaskManager httpTaskManager = (HttpTaskManager) Managers.getDefaultHttp(new URL("http://localhost:" + KVServer.PORT));



        /*httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 16.07.21", "77"); // 0
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 17.07.21", "77"); // 1

        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 18.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 19.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 20.07.21", "77");

        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 13.07.21", "20");
        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 14.07.21", "20");*/
        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 15.07.21", "20");
        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 16.07.21", "20");
        httpTaskManager.save();
        httpTaskManager.load();
        //System.out.println(httpTaskManager.getListAllTasks());


        httpTaskManager.stop();
    }

    private static void createTasks() {

    }
}