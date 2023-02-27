package managers.task;

import managers.util.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

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
        final Task task = new Task("name", "description", Status.NEW, 0);

        assertEquals(1, taskManager.getListAllTasks().size());
        assertEquals(task, taskManager.getTask(0));
    }

    @Test
    void createTasks() {
        taskManager.createTask("name", "description", Status.NEW);
        taskManager.createTask("name", "description", Status.NEW);
        final Task task = new Task("name", "description", Status.NEW, 0);
        final Task task1 = new Task("name", "description", Status.NEW, 1);

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
        taskManager.createEpic("name", "description", Status.NEW);
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

        assertEquals(0, taskManager.getTask(0).getId());
    }

    @Test
    void getTaskWithTwoElementsList() {
        taskManager.createTask("name", "description", Status.NEW);
        taskManager.createTask("name", "description", Status.NEW);

        assertEquals(0, taskManager.getTask(0).getId());
        assertEquals(1, taskManager.getTask(1).getId());
    }

    @Test
    void getSubtaskWithExistingId() {
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.createSubtask("name", "description", Status.NEW, 0);

        assertEquals(1, taskManager.getSubtask(1).getId());
    }

    @Test
    void getSubtaskWithIdMissed() {
        assertNull(taskManager.getSubtask(0));
    }

    @Test
    void getSubtaskWithTwoElementList() {
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.createSubtask("name", "description", Status.NEW, 0);
        taskManager.createSubtask("name", "description", Status.NEW, 0);

        assertEquals(1, taskManager.getSubtask(1).getId());
        assertEquals(2, taskManager.getSubtask(2).getId());
    }

    @Test
    void getEpicWithIdMissed() {
        assertNull(taskManager.getEpic(0));
    }

    @Test
    void getEpicWithExistingId() {
        taskManager.createEpic("name", "description", Status.NEW);

        assertEquals(0, taskManager.getEpic(0).getId());
    }

    @Test
    void getEpicWithTwoElementsList() {
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.createEpic("name", "description", Status.NEW);

        assertEquals(0, taskManager.getEpic(0).getId());
        assertEquals(1, taskManager.getEpic(1).getId());
    }

    @Test
    void createFirstEpic() {
        taskManager.createEpic("name", "description", Status.NEW);
        final Epic epic = new Epic("name", "description", Status.NEW, 0);

        assertEquals(1, taskManager.getListAllEpics().size());
        assertEquals(epic, taskManager.getEpic(0));
    }

    @Test
    void createEpics() {
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.createEpic("name", "description", Status.NEW);

        final Epic epic = new Epic("name", "description", Status.NEW, 0);
        final Epic epic1 = new Epic("name", "description", Status.NEW, 1);

        assertEquals(2, taskManager.getListAllEpics().size());
        assertEquals(epic, taskManager.getEpic(0));
        assertEquals(epic1, taskManager.getEpic(1));
    }

    @Test
    void createSubtaskWithoutEpic() {
        taskManager.createSubtask("name", "description", Status.NEW, 0);
        assertEquals(0, taskManager.getListAllSubtasks().size());
    }

    @Test
    void createFirstSubtask() {
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.createSubtask("name", "description", Status.NEW, 0);

        assertEquals(1, taskManager.getListAllSubtasks().size());
    }

    @Test
    void createSubtasks() {
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.createSubtask("name", "description", Status.NEW, 0);
        taskManager.createSubtask("name", "description", Status.NEW, 0);

        assertEquals(2, taskManager.getListAllSubtasks().size());
    }

    @Test
    void getTaskListWithOneElement() {
        taskManager.createTask("name", "description", Status.NEW);
        assertEquals(1, taskManager.getListAllTasks().size());
    }

    @Test
    void getEmptyTasksList() {
        assertEquals(0, taskManager.getListAllTasks().size());
    }

    @Test
    void getTaskListWithTwoElements() {
        taskManager.createTask("name", "description", Status.NEW);
        taskManager.createTask("name", "description", Status.NEW);
        assertEquals(2, taskManager.getListAllTasks().size());
    }

    @Test
    void getSubtasksListWithOneElement() {
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.createSubtask("name", "description", Status.NEW, 0);
        assertEquals(1, taskManager.getListAllSubtasks().size());
    }

    @Test
    void getSubtasksListWithTwoElements() {
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.createSubtask("name", "description", Status.NEW, 0);
        taskManager.createSubtask("name", "description", Status.NEW, 0);
        assertEquals(2, taskManager.getListAllSubtasks().size());
    }

    @Test
    void getEmptySubtasksList() {
        assertEquals(0, taskManager.getListAllSubtasks().size());
    }

    @Test
    void getEpicsListWithOneElement() {
        taskManager.createEpic("name", "description", Status.NEW);
        assertEquals(1, taskManager.getListAllEpics().size());
    }

    @Test
    void getEpicsListWithTwoElements() {
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.createEpic("name", "description", Status.NEW);
        assertEquals(2, taskManager.getListAllEpics().size());
    }

    @Test
    void getEmptyEpicsList() {
        assertEquals(0, taskManager.getListAllEpics().size());
    }
}