import managers.history.HistoryManager;
import managers.util.Managers;
import managers.task.TaskManager;
import tasks.Status;

public class Main {
    static TaskManager taskManager = Managers.getDefault();

    public static void main(String[] args) {
        createTasks();

        taskManager.getEpic(0); // запрос задач
        taskManager.getEpic(4);

        System.out.println(taskManager.getHistory()); // 0 4

        taskManager.getSubtask(1);
        taskManager.getSubtask(2);

        System.out.println(taskManager.getHistory()); // 0 4 1 2


        taskManager.getSubtask(3);

        System.out.println(taskManager.getHistory()); // 0 4 1 2 3

        taskManager.removeSubtaskById(2);
        System.out.println(taskManager.getHistory()); // 0 4 1 3

        taskManager.removeEpicById(0); // удаление эпика с 3 подзадачами
        System.out.println(taskManager.getHistory()); // 4
    }
    private static void createTasks() {
        taskManager.createEpic("Эпик 1", "Описание", Status.NEW); // id = 0
        taskManager.createSubtask("Подзадача 1", "Описание", Status.NEW, 0); // id = 1
        taskManager.createSubtask("Подзадача 2", "Описание", Status.NEW, 0); // id = 2
        taskManager.createSubtask("Подзадача 3", "Описание", Status.NEW, 0); // id = 3

        taskManager.createEpic("Эпик 2", "Описание", Status.NEW); // id = 4
    }
}
