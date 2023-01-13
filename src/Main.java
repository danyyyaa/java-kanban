import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();


        ArrayList<Integer> subtasksIdFirstEpic = new ArrayList<>();
        ArrayList<Integer> subtasksIdSecondEpic = new ArrayList<>();

        taskManager.createTask("Задача 1", "Описание", Status.NEW); // создание задач
        taskManager.createTask("Задача 2", "Описание", Status.IN_PROGRESS);

        taskManager.createEpic("Вынести мусор", "Усердно", Status.NEW);
        taskManager.createSubtask("Помыть собаку", "С мылом", Status.NEW, 2);

        taskManager.createEpic("Важный эпик 2", "Описание" , Status.NEW);
        taskManager.createSubtask("Подзадача 1", "Описание", Status.NEW, 4);
        taskManager.createSubtask("Подзадача 2", "Описание", Status.NEW, 4);


        historyManager.add(taskManager.getEpic(2));
        historyManager.add(taskManager.getEpic(2));
        historyManager.add(taskManager.getEpic(2));
        historyManager.add(taskManager.getSubtask(5));
        historyManager.add(taskManager.getSubtask(6));
        historyManager.add(taskManager.getTask(1));


        System.out.println(historyManager.getHistory());

        taskManager.calculateEpicStatus(2);

        System.out.println(historyManager.getHistory());


        Task task1 = new Task("Задача 1", "Описание", Status.IN_PROGRESS, 0); // обновление задач
        Task task2 = new Task("Задача 2", "Описание", Status.DONE, 1);
        taskManager.updateTask(0, task1);
        taskManager.updateTask(1, task2);

        System.out.println(historyManager.getHistory());

        Subtask subtask = new Subtask("Помыть собаку", "С мылом", Status.IN_PROGRESS, 3, 2);
        taskManager.updateSubtask(3, subtask);
        subtasksIdFirstEpic.add(3);
        Epic epic1 = new Epic("Вынести мусор", "Усердно", Status.IN_PROGRESS, 2, subtasksIdFirstEpic);
        taskManager.updateEpic(2, epic1);


        Subtask subtask1 = new Subtask("Подзадача 1", "Описание", Status.DONE, 5, 4);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание", Status.DONE, 6, 4);
        taskManager.updateSubtask(5, subtask1);
        taskManager.updateSubtask(6, subtask2);

        subtasksIdSecondEpic.add(5);
        subtasksIdSecondEpic.add(6);
        Epic epic2 = new Epic("Важный эпик 2", "Описание" , Status.IN_PROGRESS, 4, subtasksIdSecondEpic);
        taskManager.updateEpic(4, epic2);
        taskManager.calculateEpicStatus(4);

        System.out.println(historyManager.getHistory());


        taskManager.removeTaskById(0);
        taskManager.removeSubtaskById(3);
        taskManager.removeEpicById(4);

        System.out.println(historyManager.getHistory());
    }
}
