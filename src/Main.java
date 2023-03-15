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
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        //HttpTaskManager httpTaskManager = new HttpTaskManager(new URL("http://localhost:" + KVServer.PORT));
        HttpTaskManager httpTaskManager = new HttpTaskManager(new URL("http://localhost:" + KVServer.PORT));


        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 12.07.21", "20");
        List<Task> tasks = new ArrayList<>(httpTaskManager.getListAllTasks());
        List<Task> subtasks = new ArrayList<>(httpTaskManager.getListAllSubtasks());
        List<Task> epics = new ArrayList<>(httpTaskManager.getListAllEpics());

        for (Task task : tasks) {
            System.out.println(task);
        }

        for (Task task : subtasks) {
            System.out.println(task);
        }

        for (Task task : epics) {
            System.out.println(task);
        }

        httpTaskManager.stop();
        kvServer.stop();

    }

    private static void createTasks() {

    }
}