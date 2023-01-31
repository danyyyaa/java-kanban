public class Main {
    static TaskManager taskManager = Managers.getDefault();
    static HistoryManager historyManager = Managers.getDefaultHistory();

    public static void main(String[] args) {
        createTasks();

        historyManager.add(taskManager.getEpic(0)); // запрос задач
        historyManager.add(taskManager.getEpic(4));

        System.out.println(historyManager.getHistory()); // 0 4

        historyManager.add(taskManager.getSubtask(1));
        historyManager.add(taskManager.getSubtask(2));

        System.out.println(historyManager.getHistory()); // 0 4 1 2

        historyManager.add(taskManager.getSubtask(3));

        System.out.println(historyManager.getHistory()); // 0 4 1 2 3

        historyManager.remove(2);
        System.out.println(historyManager.getHistory()); // 0 4 1 3

        historyManager.remove(0); // удаление эпика с 3 подзадачами
        System.out.println(historyManager.getHistory()); // 4
    }
    private static void createTasks() {
        taskManager.createEpic("Эпик 1", "Описание", Status.NEW); // id = 0
        taskManager.createSubtask("Подзадача 1", "Описание", Status.NEW, 0); // id = 1
        taskManager.createSubtask("Подзадача 2", "Описание", Status.NEW, 0); // id = 2
        taskManager.createSubtask("Подзадача 3", "Описание", Status.NEW, 0); // id = 3

        taskManager.createEpic("Эпик 2", "Описание", Status.NEW); // id = 4
    }
}
