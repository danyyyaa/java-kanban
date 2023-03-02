import managers.task.TaskManager;
import managers.util.Managers;
import tasks.Status;
import tasks.Task;

public class Main {
    static TaskManager taskManager = Managers.getDefault();

    public static void main(String[] args) {
        createTasks();

        //System.out.println(taskManager.getPrioritizedTasks());

        for (Task task : taskManager.getPrioritizedTasks()) {
            //System.out.println(task.getDuration().plus(task.getDuration()));
            System.out.println(task.getId());
        }

        /*taskManager.updateSubtask(4, new Subtask("name", "description",
                Status.NEW, 4, 0, "14:09, 12.07.21", "444"));*/

        taskManager.updateTask(1, new Task("name", "description", Status.NEW, 1, "13:00, 12.07.21", "20"));



    }
    private static void createTasks() {
        /*taskManager.createEpic("name", "description", Status.NEW, "13:00, 12.07.21", "20"); // id = 0
        taskManager.createSubtask("name", "description", Status.NEW,  0, "14:09, 12.07.21", "1"); // id = 1
        taskManager.createSubtask("name", "description", Status.NEW,  0, "13:09, 14.07.21", "1"); // id = 2
        taskManager.createSubtask("name", "description", Status.NEW,  0, "15:41, 12.07.21", "1"); // id = 3
        taskManager.createSubtask("name", "description", Status.NEW,  0, "12:00, 12.07.21", "70"); // id = 4
*/

        taskManager.createTask("name", "description", Status.NEW, "12:50, 12.07.21", "20");
//        taskManager.createTask("name", "description", Status.NEW, "13:00, 12.07.21", "20");
        taskManager.createTask("name", "description", Status.NEW, "12:30, 12.07.21", "10");
    }
}
