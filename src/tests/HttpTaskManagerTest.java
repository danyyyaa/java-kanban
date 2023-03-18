package tests;

import managers.server.HttpTaskServer;
import managers.server.KVServer;
import managers.task.TaskManager;

import managers.util.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class HttpTaskManagerTest {
    private TaskManager httpTaskManager;
    private HttpTaskServer httpTaskServer;
    private KVServer kvServer;

    @BeforeEach
    public void setUp() throws IOException, InterruptedException {
        kvServer = new KVServer();
        httpTaskServer = new HttpTaskServer();

        httpTaskServer.start();
        kvServer.start();

        httpTaskManager = Managers.getDefault(new URL("http://localhost:" + KVServer.PORT));
    }

    @AfterEach
    public void afterEach() {
        httpTaskServer.stop();
        kvServer.stop();
    }

    @Test
    void calculateEpicStatusWithEmptySubtasksList() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        httpTaskManager.calculateEpicStatus(0);

        assertEquals(Status.NEW, httpTaskManager.getEpic(0).getStatus());
    }

    @Test
    void calculateEpicStatusWithNEWSubtasksStatus() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 11.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 10.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 09.07.21", "77");

        httpTaskManager.calculateEpicStatus(0);

        assertEquals(Status.NEW, httpTaskManager.getEpic(0).getStatus());
    }

    @Test
    void calculateEpicStatusWithDONESubtasksStatus() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.DONE, 0,"14:09, 11.07.21", "20");
        httpTaskManager.createSubtask("name", "description", Status.DONE, 0,"14:09, 10.07.21", "20");

        httpTaskManager.calculateEpicStatus(0);

        assertEquals(Status.DONE, httpTaskManager.getEpic(0).getStatus());
    }

    @Test
    void calculateEpicStatusWithNEWAndDONESubtasksStatus() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 12.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.DONE, 0,"14:09, 11.07.21", "20");

        httpTaskManager.calculateEpicStatus(0);

        assertEquals(Status.IN_PROGRESS, httpTaskManager.getEpic(0).getStatus());
    }

    @Test
    void calculateEpicStatusWithIN_PROGRESSSubtasksStatus() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.IN_PROGRESS, 0,"14:09, 12.07.21", "20");
        httpTaskManager.createSubtask("name", "description", Status.IN_PROGRESS, 0,"14:09, 11.07.21", "20");

        httpTaskManager.calculateEpicStatus(0);

        assertEquals(Status.IN_PROGRESS, httpTaskManager.getEpic(0).getStatus());
    }

    @Test
    void getHistoryWithStandardBehaviour() {
        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 12.07.21", "20");
        httpTaskManager.getTask(0);

        assertEquals(1, httpTaskManager.getHistory().size());
    }
    @Test
    void getHistoryWithEmptyList() {
        httpTaskManager.getTask(0);

        assertEquals(0, httpTaskManager.getHistory().size());
    }

    @Test
    void createFirstTask() {
        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 12.07.21", "20");
        final Task task = new Task("name", "description", Status.NEW, 0, "14:00, 12.07.21", "1");

        assertEquals(1, httpTaskManager.getListAllTasks().size());
        assertEquals(task, httpTaskManager.getTask(0));
    }

    @Test
    void createTasks() {
        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 12.07.21", "20");
        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 11.07.21", "20");
        final Task task = new Task("name", "description", Status.NEW, 0, "14:00, 12.07.21", "1");
        final Task task1 = new Task("name", "description", Status.NEW, 1, "14:00, 11.07.21", "1");

        assertEquals(2, httpTaskManager.getListAllTasks().size());
        assertEquals(task, httpTaskManager.getTask(0));
        assertEquals(task1, httpTaskManager.getTask(1));
    }

    @Test
    void getEmptyListAllTasks() {
        assertEquals(0, httpTaskManager.getListAllTasks().size());
    }

    @Test
    void getNotEmptyListAllTasks() {
        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 12.07.21", "20");
        assertEquals(1, httpTaskManager.getListAllTasks().size());
    }

    @Test
    void getEmptyListAllSubtasks() {
        assertEquals(0, httpTaskManager.getListAllTasks().size());
    }

    @Test
    void getNotEmptyListAllSubtasks() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 12.07.21", "77");
        assertEquals(1, httpTaskManager.getListAllSubtasks().size());
    }

    @Test
    void getEmptyListAllEpics() {
        assertEquals(0, httpTaskManager.getListAllEpics().size());
    }

    @Test
    void getNotEmptyListAllEpics() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        assertEquals(1, httpTaskManager.getListAllEpics().size());
    }

    @Test
    void getTaskWithIdMissed() {
        assertNull(httpTaskManager.getTask(0));
    }

    @Test
    void getTaskWithExistingId() {
        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 12.07.21", "20");

        assertEquals(0, httpTaskManager.getTask(0).getId());
    }

    @Test
    void getTaskWithTwoElementsList() {
        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 12.07.21", "20");
        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 11.07.21", "20");

        assertEquals(0, httpTaskManager.getTask(0).getId());
        assertEquals(1, httpTaskManager.getTask(1).getId());
    }

    @Test
    void getSubtaskWithExistingId() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 12.07.21", "77");

        assertEquals(1, httpTaskManager.getSubtask(1).getId());
    }

    @Test
    void getSubtaskWithIdMissed() {
        assertNull(httpTaskManager.getSubtask(0));
    }

    @Test
    void getSubtaskWithTwoElementList() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 11.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 10.07.21", "77");

        assertEquals(1, httpTaskManager.getSubtask(1).getId());
        assertEquals(2, httpTaskManager.getSubtask(2).getId());
    }

    @Test
    void getEpicWithIdMissed() {
        assertNull(httpTaskManager.getEpic(0));
    }

    @Test
    void getEpicWithExistingId() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");

        assertEquals(0, httpTaskManager.getEpic(0).getId());
    }

    @Test
    void getEpicWithTwoElementsList() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");

        assertEquals(0, httpTaskManager.getEpic(0).getId());
        assertEquals(1, httpTaskManager.getEpic(1).getId());
    }

    @Test
    void createFirstEpic() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        final Epic epic = new Epic("name", "description", Status.NEW, 0, "14:00, 12.07.21", "1");

        assertEquals(1, httpTaskManager.getListAllEpics().size());
        assertEquals(epic, httpTaskManager.getEpic(0));
    }

    @Test
    void createEpics() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");

        final Epic epic = new Epic("name", "description", Status.NEW, 0, "14:00, 12.07.21", "1");
        final Epic epic1 = new Epic("name", "description", Status.NEW, 1, "14:00, 12.07.21", "1");

        assertEquals(2, httpTaskManager.getListAllEpics().size());
        assertEquals(epic, httpTaskManager.getEpic(0));
        assertEquals(epic1, httpTaskManager.getEpic(1));
    }

    @Test
    void createSubtaskWithoutEpic() {
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 12.07.21", "77");
        assertEquals(0, httpTaskManager.getListAllSubtasks().size());
    }

    @Test
    void createFirstSubtask() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 12.07.21", "77");

        assertEquals(1, httpTaskManager.getListAllSubtasks().size());
    }

    @Test
    void createSubtasks() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 11.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 10.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 09.07.21", "77");

        assertEquals(2, httpTaskManager.getListAllSubtasks().size());
    }

    @Test
    void getTaskListWithOneElement() {
        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 12.07.21", "20");
        assertEquals(1, httpTaskManager.getListAllTasks().size());
    }

    @Test
    void getEmptyTasksList() {
        assertEquals(0, httpTaskManager.getListAllTasks().size());
    }

    @Test
    void getTaskListWithTwoElements() {
        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 12.07.21", "20");
        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 11.07.21", "20");
        assertEquals(2, httpTaskManager.getListAllTasks().size());
    }

    @Test
    void getSubtasksListWithOneElement() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 12.07.21", "77");
        assertEquals(1, httpTaskManager.getListAllSubtasks().size());
    }

    @Test
    void getSubtasksListWithTwoElements() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 11.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 10.07.21", "77");
        assertEquals(2, httpTaskManager.getListAllSubtasks().size());
    }

    @Test
    void getEmptySubtasksList() {
        assertEquals(0, httpTaskManager.getListAllSubtasks().size());
    }

    @Test
    void getEpicsListWithOneElement() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        assertEquals(1, httpTaskManager.getListAllEpics().size());
    }

    @Test
    void getEpicsListWithTwoElements() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        assertEquals(2, httpTaskManager.getListAllEpics().size());
    }

    @Test
    void getEmptyEpicsList() {
        assertEquals(0, httpTaskManager.getListAllEpics().size());
    }

    @Test
    void updateTaskMissedId() {
        httpTaskManager.updateTask(0, new Task("name", "description", Status.NEW, 1, "14:00, 12.07.21", "1"));
        assertEquals(0, httpTaskManager.getListAllTasks().size());
    }

    @Test
    void updateTaskWithEmptyList() {
        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 12.07.21", "20");
        final Task task = new Task("name", "description", Status.NEW, 0, "14:00, 12.07.21", "1");

        httpTaskManager.updateTask(0, new Task("name", "description", Status.NEW, 0, "14:09, 13.07.21", "77"));

        assertEquals(0, httpTaskManager.getTask(0).getId());
        assertEquals(task, httpTaskManager.getTask(0));
    }

    @Test
    void updateTaskWithNotEmptyList() {
        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 12.07.21", "1");
        httpTaskManager.createTask("name", "description", Status.IN_PROGRESS,"14:09, 11.07.21", "1");

        final Task task = new Task("name", "description", Status.NEW, 0, "14:00, 12.07.21", "1");
        final Task task1 = new Task("name", "description", Status.IN_PROGRESS, 1, "14:00, 11.07.21", "1");

        httpTaskManager.updateTask(0, new Task("name", "description", Status.NEW, 0, "14:00, 12.07.21", "1"));
        httpTaskManager.updateTask(1, new Task("name", "description", Status.IN_PROGRESS, 1, "14:00, 11.07.21", "1"));

        assertEquals(0, httpTaskManager.getTask(0).getId());
        assertEquals(1, httpTaskManager.getTask(1).getId());
        assertEquals(task, httpTaskManager.getTask(0));
        assertEquals(task1, httpTaskManager.getTask(1));
    }

    @Test
    void updateSubtaskMissedId() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        httpTaskManager.updateSubtask(0, new Subtask("name", "description", Status.NEW, 1, 0, "14:00, 12.07.21", "1"));
        assertEquals(0, httpTaskManager.getListAllSubtasks().size());
    }

    @Test
    void updateSubtaskWithEmptyList() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 12.07.21", "77");
        final Subtask subtask = new Subtask("name", "description", Status.NEW, 1, 0, "14:00, 12.07.21", "1");

        httpTaskManager.updateSubtask(1, new Subtask("name", "description", Status.NEW, 1, 0, "14:09, 13.07.21", "77"));

        assertEquals(1, httpTaskManager.getSubtask(1).getId());
        assertEquals(subtask, httpTaskManager.getSubtask(1));
    }

    @Test
    void updateSubtaskWithNotEmptyList() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "1");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 11.07.21", "1");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 10.07.21", "1");
        final Subtask subtask = new Subtask("name", "description", Status.NEW, 1, 0, "14:00, 11.07.21", "1");
        final Subtask subtask1 = new Subtask("name", "description", Status.NEW, 2, 0, "14:00, 10.07.21", "1");

        httpTaskManager.updateSubtask(1, new Subtask("name", "description", Status.NEW, 1, 0, "14:00, 11.07.21", "1"));
        httpTaskManager.updateSubtask(2, new Subtask("name", "description", Status.NEW, 2, 0, "14:00, 10.07.21", "1"));

        assertEquals(1, httpTaskManager.getSubtask(1).getId());
        assertEquals(2, httpTaskManager.getSubtask(2).getId());
        assertEquals(subtask, httpTaskManager.getSubtask(1));
        assertEquals(subtask1, httpTaskManager.getSubtask(2));
    }

    @Test
    void updateEpicMissedId() {
        httpTaskManager.updateEpic(0, new Epic("name", "description", Status.NEW, 0, "14:00, 12.07.21", "1"));
        assertEquals(0, httpTaskManager.getListAllEpics().size());
    }

    @Test
    void updateEpicWithEmptyList() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        final Epic epic = new Epic("name", "description", Status.NEW, 0, "14:00, 12.07.21", "1");

        httpTaskManager.updateEpic(0, new Epic("name", "description", Status.NEW, 0, "14:09, 13.07.21", "77"));

        assertEquals(0, httpTaskManager.getEpic(0).getId());
        assertEquals(epic, httpTaskManager.getEpic(0));
    }

    @Test
    void updateEpicWithNotEmptyList() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");

        final Epic epic = new Epic("name", "description", Status.NEW, 0, "14:00, 12.07.21", "1");
        final Epic epic1 = new Epic("name", "description", Status.NEW, 1, "14:00, 12.07.21", "1");

        httpTaskManager.updateEpic(0, new Epic("name", "description", Status.NEW, 0, "14:09, 13.07.21", "77"));
        httpTaskManager.updateEpic(1, new Epic("name", "description", Status.NEW, 1, "14:09, 13.07.21", "77"));

        assertEquals(0, httpTaskManager.getEpic(0).getId());
        assertEquals(1, httpTaskManager.getEpic(1).getId());

        assertEquals(epic, httpTaskManager.getEpic(0));
        assertEquals(epic1, httpTaskManager.getEpic(1));
    }

    @Test
    void getAnyTaskByIdWithIdMissed() {
        assertNull(httpTaskManager.getAnyTaskById(0));
    }

    @Test
    void getAnyTaskByIdWithExistingId() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77"); // id = 0
        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 11.07.21", "20"); // id = 1
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 10.07.21", "77"); // id = 2

        assertEquals(0, httpTaskManager.getAnyTaskById(0).getId());
        assertEquals(1, httpTaskManager.getAnyTaskById(1).getId());
        assertEquals(2, httpTaskManager.getAnyTaskById(2).getId());
    }

    @Test
    void getAnyTaskByIdWithNotEmptyList() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77"); // id = 0
        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 11.07.21", "20"); // id = 1
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 10.07.21", "77"); // id = 2

        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 09.07.21", "77"); // id = 3
        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 08.07.21", "20"); // id = 4
        httpTaskManager.createSubtask("name", "description", Status.NEW, 3,"14:09, 07.07.21", "20"); // id = 5

        assertEquals(0, httpTaskManager.getAnyTaskById(0).getId());
        assertEquals(1, httpTaskManager.getAnyTaskById(1).getId());
        assertEquals(2, httpTaskManager.getAnyTaskById(2).getId());
        assertEquals(3, httpTaskManager.getAnyTaskById(3).getId());
        assertEquals(4, httpTaskManager.getAnyTaskById(4).getId());
        assertEquals(5, httpTaskManager.getAnyTaskById(5).getId());
    }

    @Test
    void deleteAllTasksWithEmptyList() {
        httpTaskManager.deleteAllTasks();
        assertEquals(0, httpTaskManager.getListAllTasks().size());
    }

    @Test
    void deleteAllTasksWithOneElementInList() {
        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 12.07.21", "20");
        httpTaskManager.deleteAllTasks();

        assertEquals(0, httpTaskManager.getListAllTasks().size());
    }

    @Test
    void deleteAllTasksWithTwoElementsInList() {
        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 11.07.21", "20");
        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 10.07.21", "20");
        httpTaskManager.deleteAllTasks();
        assertEquals(0, httpTaskManager.getListAllTasks().size());
    }

    @Test
    void deleteAllSubtasksWithEmptyList() {
        httpTaskManager.deleteAllSubtasks();
        assertEquals(0, httpTaskManager.getListAllSubtasks().size());
    }

    @Test
    void deleteAllSubtasksWithOneElementInList() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 12.07.21", "77");
        httpTaskManager.deleteAllSubtasks();

        assertEquals(0, httpTaskManager.getEpic(0).getSubtasksId().size());
        assertEquals(0, httpTaskManager.getListAllSubtasks().size());
    }

    @Test
    void deleteAllSubtasksWithTwoElementInList() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 10.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 12.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 11.07.21", "77");
        httpTaskManager.deleteAllSubtasks();

        assertEquals(0, httpTaskManager.getEpic(0).getSubtasksId().size());
        assertEquals(0, httpTaskManager.getListAllTasks().size());
    }

    @Test
    void deleteAllEpicsWithEmptyList() {
        httpTaskManager.deleteAllEpics();
        assertEquals(0, httpTaskManager.getListAllEpics().size());
    }

    @Test
    void deleteAllEpicsWithOneElementInList() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        httpTaskManager.deleteAllEpics();

        assertEquals(0, httpTaskManager.getListAllEpics().size());
    }

    @Test
    void deleteAllEpicsWithTwoElementInListAndWithSubtasks() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 11.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 10.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 09.07.21", "77");

        httpTaskManager.deleteAllEpics();
        assertEquals(0, httpTaskManager.getListAllEpics().size());
        assertEquals(0, httpTaskManager.getListAllSubtasks().size());
    }

    @Test
    void removeTaskByIdWithMissedId() {
        httpTaskManager.removeTaskById(0);
        assertEquals(0, httpTaskManager.getListAllTasks().size());
    }

    @Test
    void removeTaskByIdWithOneElementInList() {
        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 12.07.21", "20");
        httpTaskManager.removeTaskById(0);
        assertEquals(0, httpTaskManager.getListAllTasks().size());
    }

    @Test
    void removeTaskByIdWithTwoElementsInList() {
        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 12.07.21", "20");
        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 11.07.21", "20");
        httpTaskManager.removeTaskById(0);
        httpTaskManager.removeTaskById(1);
        assertEquals(0, httpTaskManager.getListAllTasks().size());
    }

    @Test
    void removeTaskByIdWithTreeElementsInList() {
        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 11.07.21", "20");
        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 10.07.21", "20");
        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 09.07.21", "20");
        httpTaskManager.removeTaskById(1);

        assertEquals(2, httpTaskManager.getListAllTasks().size());
    }

    @Test
    void removeSubtaskByIdWithMissedId() {
        httpTaskManager.removeSubtaskById(0);
        assertEquals(0, httpTaskManager.getListAllSubtasks().size());
    }

    @Test
    void removeSubtaskByIdWithOneElementInList() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 12.07.21", "77");

        httpTaskManager.removeSubtaskById(1);
        assertEquals(0, httpTaskManager.getListAllSubtasks().size());
    }

    @Test
    void removeSubtaskByIdWithTwoElementsInList() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 11.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 10.07.21", "77");

        httpTaskManager.removeSubtaskById(1);
        httpTaskManager.removeSubtaskById(2);

        assertEquals(0, httpTaskManager.getListAllSubtasks().size());
    }

    @Test
    void removeSubtaskByIdWithTreeElementsInList() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 11.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 10.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 09.07.21", "77");

        httpTaskManager.removeSubtaskById(2);

        assertEquals(2, httpTaskManager.getListAllSubtasks().size());
    }

    @Test
    void removeEpicByIdWithMissedId() {
        httpTaskManager.removeEpicById(0);
        assertEquals(0, httpTaskManager.getListAllEpics().size());
    }

    @Test
    void removeEpicByIdWithOneElementInList() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        httpTaskManager.removeEpicById(0);
        assertEquals(0, httpTaskManager.getListAllEpics().size());
    }

    @Test
    void removeEpicByIdWithTwoElementInList() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        httpTaskManager.removeEpicById(0);
        httpTaskManager.removeEpicById(1);
        assertEquals(0, httpTaskManager.getListAllEpics().size());
    }

    @Test
    void removeEpicByIdWithTreeElementInList() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        httpTaskManager.removeEpicById(1);

        assertEquals(2, httpTaskManager.getListAllEpics().size());
    }

    @Test
    void removeNotEmptyEpicById() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 11.07.21", "77");
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 10.07.21", "77");

        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 09.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 1,"14:09, 08.07.21", "20");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 1,"14:09, 07.07.21", "20");

        httpTaskManager.removeEpicById(0);
        httpTaskManager.removeEpicById(1);
        httpTaskManager.removeEpicById(2);

        assertEquals(0, httpTaskManager.getListAllEpics().size());
        assertEquals(0, httpTaskManager.getListAllSubtasks().size());
    }

    @Test
    void removeAnyTaskByIdWithOneElementInMap() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 11.07.21", "77");
        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 10.07.21", "20");

        httpTaskManager.removeAnyTaskById(0);
        httpTaskManager.removeAnyTaskById(1);
        httpTaskManager.removeAnyTaskById(2);

        assertEquals(0,httpTaskManager.getListAllTasks().size());
        assertEquals(0, httpTaskManager.getListAllSubtasks().size());
        assertEquals(0, httpTaskManager.getListAllEpics().size());
    }

    @Test
    void removeAnyTaskByIdWithMissedId() {
        httpTaskManager.removeAnyTaskById(0);
        httpTaskManager.removeAnyTaskById(1);
        httpTaskManager.removeAnyTaskById(2);

        assertEquals(0,httpTaskManager.getListAllTasks().size());
        assertEquals(0, httpTaskManager.getListAllSubtasks().size());
        assertEquals(0, httpTaskManager.getListAllEpics().size());
    }

    @Test
    void removeAnyTaskByIdWithTwoElementsInMap() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 11.07.21", "77");
        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 10.07.21", "20");

        httpTaskManager.createEpic("name", "description", Status.NEW, "14:09, 09.07.21", "77");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 3, "14:09, 08.07.21", "77");
        httpTaskManager.createTask("name", "description", Status.NEW,"14:09, 07.07.21", "20");

        httpTaskManager.removeAnyTaskById(0);
        httpTaskManager.removeAnyTaskById(1);
        httpTaskManager.removeAnyTaskById(2);
        httpTaskManager.removeAnyTaskById(3);
        httpTaskManager.removeAnyTaskById(4);
        httpTaskManager.removeAnyTaskById(5);

        assertEquals(0,httpTaskManager.getListAllTasks().size());
        assertEquals(0, httpTaskManager.getListAllSubtasks().size());
        assertEquals(0, httpTaskManager.getListAllEpics().size());
    }

    @Test
    void calculateEpicStartTime() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "15:09, 12.07.21", "77");

        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:00, 12.07.21", "1");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 12.07.21", "1");

        httpTaskManager.calculateEpicTime(0);
        assertEquals("2021-07-12T14:00", httpTaskManager.getEpic(0).getStartTime().toString());
    }

    @Test
    void calculateEpicEndTime() {
        httpTaskManager.createEpic("name", "description", Status.NEW, "15:09, 10.07.21", "77");

        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:00, 12.07.21", "1");
        httpTaskManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 12.07.21", "1");

        httpTaskManager.calculateEpicTime(0);
        assertEquals("2021-07-12T14:10", httpTaskManager.getEpic(0).getEndTime().toString());
    }

    @Test
    void getPrioritizedTasks() {
        httpTaskManager.createTask("name", "description", Status.NEW,"13:10, 12.07.21", "1");
        httpTaskManager.createTask("name", "description", Status.NEW,"13:00, 12.07.21", "1");
        httpTaskManager.createTask("name", "description", Status.NEW,"13:05, 12.07.21", "1");
        httpTaskManager.createTask("name", "description", Status.NEW,"13:15, 12.07.21", "1");

        StringBuilder result = new StringBuilder();
        for (Task task : httpTaskManager.getPrioritizedTasks()) {
            result.append(task.getId());
        }

        assertEquals("1203", result.toString());
    }
}


