import managers.file.FileBackedTasksManager;
import managers.file.ManagerSaveException;
import managers.server.HttpTaskServer;
import managers.server.KVServer;
import managers.task.TaskManager;
import managers.util.Managers;
import tasks.Status;
import tasks.Task;
import java.io.IOException;


import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        //createTasks();
        //HttpTaskServer httpTaskServer = new HttpTaskServer();
        new KVServer().start();


    }

    private static void createTasks() {

    }
}