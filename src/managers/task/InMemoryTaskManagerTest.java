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
        taskManager.createEpic("name", "description", Status.NEW); // id = 0
        taskManager.calculateEpicStatus(0);

        assertEquals(Status.NEW, taskManager.getEpic(0).getStatus());
    }

    @Test
    void calculateEpicStatusWithNEWSubtasksStatus() {
        taskManager.createEpic("name", "description", Status.NEW); // id = 0
        taskManager.createSubtask("name", "description", Status.NEW, 0);
        taskManager.createSubtask("name", "description", Status.NEW, 0);
        taskManager.createSubtask("name", "description", Status.NEW, 0);

        taskManager.calculateEpicStatus(0);

        assertEquals(Status.NEW, taskManager.getEpic(0).getStatus());

    }

    @Test
    void calculateEpicStatusWithDONESubtasksStatus() {
        taskManager.createEpic("name", "description", Status.NEW); // id = 0
        taskManager.createSubtask("name", "description", Status.DONE, 0);
        taskManager.createSubtask("name", "description", Status.DONE, 0);

        taskManager.calculateEpicStatus(0);

        assertEquals(Status.DONE, taskManager.getEpic(0).getStatus());
    }

    @Test
    void calculateEpicStatusWithNEWAndDONESubtasksStatus() {
        taskManager.createEpic("name", "description", Status.NEW); // id = 0
        taskManager.createSubtask("name", "description", Status.NEW, 0);
        taskManager.createSubtask("name", "description", Status.DONE, 0);

        taskManager.calculateEpicStatus(0);

        assertEquals(Status.IN_PROGRESS, taskManager.getEpic(0).getStatus());
    }

    @Test
    void calculateEpicStatusWithIN_PROGRESSSubtasksStatus() {
        taskManager.createEpic("name", "description", Status.NEW); // id = 0
        taskManager.createSubtask("name", "description", Status.IN_PROGRESS, 0);
        taskManager.createSubtask("name", "description", Status.IN_PROGRESS, 0);

        taskManager.calculateEpicStatus(0);

        assertEquals(Status.IN_PROGRESS, taskManager.getEpic(0).getStatus());
    }






    /*@Test
    void addNewTask() {
        taskManager.createTask("name", "description", Status.NEW); // id = 0
        final Task task = taskManager.getTask(0);
        assertEquals(1, taskManager.getListAllTasks().size());
    }*/
}