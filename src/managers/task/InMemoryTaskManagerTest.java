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

    @Test
    void updateTaskMissedId() {
        taskManager.updateTask(0, new Task("name", "description", Status.NEW, 1));
        assertEquals(0, taskManager.getListAllTasks().size());
    }

    @Test
    void updateTaskWithEmptyList() {
        taskManager.createTask("name", "description", Status.NEW);
        final Task task = new Task("name", "description", Status.NEW, 0);

        taskManager.updateTask(0, new Task("name", "description", Status.NEW, 0));

        assertEquals(0, taskManager.getTask(0).getId());
        assertEquals(task, taskManager.getTask(0));
    }

    @Test
    void updateTaskWithNotEmptyList() {
        taskManager.createTask("name", "description", Status.NEW);
        taskManager.createTask("name", "description", Status.IN_PROGRESS);

        final Task task = new Task("name", "description", Status.NEW, 0);
        final Task task1 = new Task("name", "description", Status.IN_PROGRESS, 1);

        taskManager.updateTask(0, new Task("name", "description", Status.NEW, 0));
        taskManager.updateTask(1, new Task("name", "description", Status.IN_PROGRESS, 1));

        assertEquals(0, taskManager.getTask(0).getId());
        assertEquals(1, taskManager.getTask(1).getId());
        assertEquals(task, taskManager.getTask(0));
        assertEquals(task1, taskManager.getTask(1));
    }

    @Test
    void updateSubtaskMissedId() {
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.updateSubtask(0, new Subtask("name", "description", Status.NEW, 1, 0));
        assertEquals(0, taskManager.getListAllSubtasks().size());
    }

    @Test
    void updateSubtaskWithEmptyList() {
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.createSubtask("name", "description", Status.NEW, 0);
        final Subtask subtask = new Subtask("name", "description", Status.NEW, 1, 0);

        taskManager.updateSubtask(1, new Subtask("name", "description", Status.NEW, 1, 0));

        assertEquals(1, taskManager.getSubtask(1).getId());
        assertEquals(subtask, taskManager.getSubtask(1));
    }

    @Test
    void updateSubtaskWithNotEmptyList() {
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.createSubtask("name", "description", Status.NEW, 0);
        taskManager.createSubtask("name", "description", Status.NEW, 0);
        final Subtask subtask = new Subtask("name", "description", Status.NEW, 1, 0);
        final Subtask subtask1 = new Subtask("name", "description", Status.NEW, 2, 0);

        taskManager.updateSubtask(1, new Subtask("name", "description", Status.NEW, 1, 0));
        taskManager.updateSubtask(2, new Subtask("name", "description", Status.NEW, 2, 0));

        assertEquals(1, taskManager.getSubtask(1).getId());
        assertEquals(2, taskManager.getSubtask(2).getId());
        assertEquals(subtask, taskManager.getSubtask(1));
        assertEquals(subtask1, taskManager.getSubtask(2));
    }

    @Test
    void updateEpicMissedId() {
        taskManager.updateEpic(0, new Epic("name", "description", Status.NEW, 0));
        assertEquals(0, taskManager.getListAllEpics().size());
    }

    @Test
    void updateEpicWithEmptyList() {
        taskManager.createEpic("name", "description", Status.NEW);
        final Epic epic = new Epic("name", "description", Status.NEW, 0);

        taskManager.updateEpic(0, new Epic("name", "description", Status.NEW, 0));

        assertEquals(0, taskManager.getEpic(0).getId());
        assertEquals(epic, taskManager.getEpic(0));
    }

    @Test
    void updateEpicWithNotEmptyList() {
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.createEpic("name", "description", Status.NEW);

        final Epic epic = new Epic("name", "description", Status.NEW, 0);
        final Epic epic1 = new Epic("name", "description", Status.NEW, 1);

        taskManager.updateEpic(0, new Epic("name", "description", Status.NEW, 0));
        taskManager.updateEpic(1, new Epic("name", "description", Status.NEW, 1));

        assertEquals(0, taskManager.getEpic(0).getId());
        assertEquals(1, taskManager.getEpic(1).getId());

        assertEquals(epic, taskManager.getEpic(0));
        assertEquals(epic1, taskManager.getEpic(1));
    }

    @Test
    void getAnyTaskByIdWithIdMissed() {
        assertNull(taskManager.getAnyTaskById(0));
    }

    @Test
    void getAnyTaskByIdWithExistingId() {
        taskManager.createEpic("name", "description", Status.NEW); // id = 0
        taskManager.createTask("name", "description", Status.NEW); // id = 1
        taskManager.createSubtask("name", "description", Status.NEW, 0); // id = 2

        assertEquals(0, taskManager.getAnyTaskById(0).getId());
        assertEquals(1, taskManager.getAnyTaskById(1).getId());
        assertEquals(2, taskManager.getAnyTaskById(2).getId());
    }

    @Test
    void getAnyTaskByIdWithNotEmptyList() {
        taskManager.createEpic("name", "description", Status.NEW); // id = 0
        taskManager.createTask("name", "description", Status.NEW); // id = 1
        taskManager.createSubtask("name", "description", Status.NEW, 0); // id = 2

        taskManager.createEpic("name", "description", Status.NEW); // id = 3
        taskManager.createTask("name", "description", Status.NEW); // id = 4
        taskManager.createSubtask("name", "description", Status.NEW, 3); // id = 5

        assertEquals(0, taskManager.getAnyTaskById(0).getId());
        assertEquals(1, taskManager.getAnyTaskById(1).getId());
        assertEquals(2, taskManager.getAnyTaskById(2).getId());
        assertEquals(3, taskManager.getAnyTaskById(3).getId());
        assertEquals(4, taskManager.getAnyTaskById(4).getId());
        assertEquals(5, taskManager.getAnyTaskById(5).getId());
    }

    @Test
    void deleteAllTasksWithEmptyList() {
        taskManager.deleteAllTasks();
        assertEquals(0, taskManager.getListAllTasks().size());
    }

    @Test
    void deleteAllTasksWithOneElementInList() {
        taskManager.createTask("name", "description", Status.NEW);
        taskManager.deleteAllTasks();

        assertEquals(0, taskManager.getListAllTasks().size());
    }

    @Test
    void deleteAllTasksWithTwoElementsInList() {
        taskManager.createTask("name", "description", Status.NEW);
        taskManager.createTask("name", "description", Status.NEW);
        taskManager.deleteAllTasks();
        assertEquals(0, taskManager.getListAllTasks().size());
    }

    @Test
    void deleteAllSubtasksWithEmptyList() {
        taskManager.deleteAllSubtasks();
        assertEquals(0, taskManager.getListAllSubtasks().size());
    }

    @Test
    void deleteAllSubtasksWithOneElementInList() {
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.createSubtask("name", "description", Status.NEW, 0);
        taskManager.deleteAllSubtasks();

        assertEquals(0, taskManager.getEpic(0).getSubtasksId().size());
        assertEquals(0, taskManager.getListAllSubtasks().size());
    }

    @Test
    void deleteAllTasksWithTwoElementInList() {
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.createSubtask("name", "description", Status.NEW, 0);
        taskManager.createSubtask("name", "description", Status.NEW, 0);
        taskManager.deleteAllSubtasks();

        assertEquals(0, taskManager.getEpic(0).getSubtasksId().size());
        assertEquals(0, taskManager.getListAllTasks().size());
    }

    @Test
    void deleteAllEpicsWithEmptyList() {
        taskManager.deleteAllEpics();
        assertEquals(0, taskManager.getListAllEpics().size());
    }

    @Test
    void deleteAllEpicsWithOneElementInList() {
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.deleteAllEpics();

        assertEquals(0, taskManager.getListAllEpics().size());
    }

    @Test
    void deleteAllEpicsWithTwoElementInListAndWithSubtasks() {
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.createSubtask("name", "description", Status.NEW, 0);
        taskManager.createSubtask("name", "description", Status.NEW, 0);

        taskManager.deleteAllEpics();
        assertEquals(0, taskManager.getListAllEpics().size());
        assertEquals(0, taskManager.getListAllSubtasks().size());
    }

    @Test
    void removeTaskByIdWithMissedId() {
        taskManager.removeTaskById(0);
        assertEquals(0, taskManager.getListAllTasks().size());
    }

    @Test
    void removeTaskByIdWithOneElementInList() {
        taskManager.createTask("name", "description", Status.NEW);
        taskManager.removeTaskById(0);
        assertEquals(0, taskManager.getListAllTasks().size());
    }

    @Test
    void removeTaskByIdWithTwoElementsInList() {
        taskManager.createTask("name", "description", Status.NEW);
        taskManager.createTask("name", "description", Status.NEW);
        taskManager.removeTaskById(0);
        taskManager.removeTaskById(1);
        assertEquals(0, taskManager.getListAllTasks().size());
    }

    @Test
    void removeTaskByIdWithTreeElementsInList() {
        taskManager.createTask("name", "description", Status.NEW);
        taskManager.createTask("name", "description", Status.NEW);
        taskManager.createTask("name", "description", Status.NEW);
        taskManager.removeTaskById(1);

        assertEquals(2, taskManager.getListAllTasks().size());
    }

    @Test
    void removeSubtaskByIdWithMissedId() {
        taskManager.removeSubtaskById(0);
        assertEquals(0, taskManager.getListAllSubtasks().size());
    }

    @Test
    void removeSubtaskByIdWithOneElementInList() {
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.createSubtask("name", "description", Status.NEW, 0);

        taskManager.removeSubtaskById(1);
        assertEquals(0, taskManager.getListAllSubtasks().size());
    }

    @Test
    void removeSubtaskByIdWithTwoElementsInList() {
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.createSubtask("name", "description", Status.NEW, 0);
        taskManager.createSubtask("name", "description", Status.NEW, 0);

        taskManager.removeSubtaskById(1);
        taskManager.removeSubtaskById(2);

        assertEquals(0, taskManager.getListAllSubtasks().size());
    }

    @Test
    void removeSubtaskByIdWithTreeElementsInList() {
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.createSubtask("name", "description", Status.NEW, 0);
        taskManager.createSubtask("name", "description", Status.NEW, 0);
        taskManager.createSubtask("name", "description", Status.NEW, 0);

        taskManager.removeSubtaskById(2);


        assertEquals(2, taskManager.getListAllSubtasks().size());
    }

    @Test
    void removeEpicByIdWithMissedId() {
        taskManager.removeEpicById(0);
        assertEquals(0, taskManager.getListAllEpics().size());
    }

    @Test
    void removeEpicByIdWithOneElementInList() {
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.removeEpicById(0);
        assertEquals(0, taskManager.getListAllEpics().size());
    }

    @Test
    void removeEpicByIdWithTwoElementInList() {
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.removeEpicById(0);
        taskManager.removeEpicById(1);
        assertEquals(0, taskManager.getListAllEpics().size());
    }

    @Test
    void removeEpicByIdWithTreeElementInList() {
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.removeEpicById(1);

        assertEquals(2, taskManager.getListAllEpics().size());
    }

    @Test
    void removeNotEmptyEpicById() {
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.createEpic("name", "description", Status.NEW);
        taskManager.createEpic("name", "description", Status.NEW);

        taskManager.createSubtask("name", "description", Status.NEW, 0);
        taskManager.createSubtask("name", "description", Status.NEW, 1);
        taskManager.createSubtask("name", "description", Status.NEW, 1);

        taskManager.removeEpicById(0);
        taskManager.removeEpicById(1);
        taskManager.removeEpicById(2);

        assertEquals(0, taskManager.getListAllEpics().size());
        assertEquals(0, taskManager.getListAllSubtasks().size());
    }
}