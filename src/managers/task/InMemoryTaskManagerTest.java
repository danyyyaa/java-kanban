package managers.task;

import managers.util.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    public void create() {
        taskManager = Managers.getDefault();
    }

    @Test
    void calculateEpicStatusWithEmptySubtasksList() {
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.calculateEpicStatus(0);

        assertEquals(Status.NEW, taskManager.getEpic(0).getStatus());
    }

    @Test
    void calculateEpicStatusWithNEWSubtasksStatus() {
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.createSubtask("name", "description", Status.NEW, 0);
        taskManager.createSubtask("name", "description", Status.NEW, 0);
        taskManager.createSubtask("name", "description", Status.NEW, 0);

        taskManager.calculateEpicStatus(0);

        assertEquals(Status.NEW, taskManager.getEpic(0).getStatus());

    }

    @Test
    void calculateEpicStatusWithDONESubtasksStatus() {
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.createSubtask("name", "description", Status.DONE, 0);
        taskManager.createSubtask("name", "description", Status.DONE, 0);

        taskManager.calculateEpicStatus(0);

        assertEquals(Status.DONE, taskManager.getEpic(0).getStatus());
    }

    @Test
    void calculateEpicStatusWithNEWAndDONESubtasksStatus() {
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.createSubtask("name", "description", Status.NEW, 0);
        taskManager.createSubtask("name", "description", Status.DONE, 0);

        taskManager.calculateEpicStatus(0);

        assertEquals(Status.IN_PROGRESS, taskManager.getEpic(0).getStatus());
    }

    @Test
    void calculateEpicStatusWithIN_PROGRESSSubtasksStatus() {
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.createSubtask("name", "description", Status.IN_PROGRESS, 0);
        taskManager.createSubtask("name", "description", Status.IN_PROGRESS, 0);

        taskManager.calculateEpicStatus(0);

        assertEquals(Status.IN_PROGRESS, taskManager.getEpic(0).getStatus());
    }

    @Test
    void getHistoryWithStandardBehaviour() {
        taskManager.createTask("name", "description", Status.NEW);
        taskManager.getTask(0);

        assertEquals(1, taskManager.getHistory().size());
    }
    @Test
    void getHistoryWithEmptyList() {
        taskManager.getTask(0);

        assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    void createFirstTask() {
        taskManager.createTask("name", "description", Status.NEW);
        final Task task = taskManager.getTask(0);

        assertEquals(1, taskManager.getListAllTasks().size());
        assertEquals(task, taskManager.getTask(0));
    }

    @Test
    void createTasks() {
        taskManager.createTask("name", "description", Status.NEW);
        taskManager.createTask("name", "description", Status.NEW);
        final Task task = taskManager.getTask(0);
        final Task task1 = taskManager.getTask(1);

        assertEquals(2, taskManager.getListAllTasks().size());
        assertEquals(task, taskManager.getTask(0));
        assertEquals(task1, taskManager.getTask(1));
    }

    @Test
    void getEmptyListAllTasks() {
        assertEquals(0, taskManager.getListAllTasks().size());
    }

    @Test
    void getNotEmptyListAllTasks() {
        taskManager.createTask("name", "description", Status.NEW);
        assertEquals(1, taskManager.getListAllTasks().size());
    }

    @Test
    void getEmptyListAllSubtasks() {
        assertEquals(0, taskManager.getListAllTasks().size());
    }

    @Test
    void getNotEmptyListAllSubtasks() {
        taskManager.createSubtask("name", "description", Status.NEW, 0);
        assertEquals(1, taskManager.getListAllSubtasks().size());
    }

    @Test
    void getEmptyListAllEpics() {
        assertEquals(0, taskManager.getListAllEpics().size());
    }

    @Test
    void getNotEmptyListAllEpics() {
        taskManager.createEpic("name", "description", Status.NEW);
        assertEquals(1, taskManager.getListAllEpics().size());
    }

    @Test
    void getTaskWithIdMissed() {
        assertNull(taskManager.getTask(0));
    }

    @Test
    void getTaskWithExistingId() {
        taskManager.createTask("name", "description", Status.NEW);

        assertEquals(1, taskManager.getListAllTasks().size());
    }

    @Test
    void getTaskWithCorrectEquals() {
        taskManager.createTask("name", "description", Status.NEW);
        taskManager.createTask("name", "description", Status.NEW);

        assertEquals(2, taskManager.getListAllTasks().size());
    }
}