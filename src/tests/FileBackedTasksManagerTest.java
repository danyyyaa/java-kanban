package tests;

import org.junit.jupiter.api.AfterEach;
import tasks.Subtask;



import managers.file.FileBackedTasksManager;
import managers.file.ManagerSaveException;

import managers.util.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FileBackedTasksManagerTest {
    private FileBackedTasksManager fileManager;
    final String path = "src/tests data.csv";

    @BeforeEach
    public void setUp() {
        try {
            fileManager = Managers.FileManager(Path.of(path));
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void afterEach() {
        try {
            Files.delete(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createFirstTask() {
        fileManager.createTask("name", "description", Status.NEW,"14:09, 12.07.21", "20");
        final Task task = new Task("name", "description", Status.NEW, 0, "14:00, 12.07.21", "1");

        assertEquals(1, fileManager.getListAllTasks().size());
        assertEquals(task, fileManager.getTask(0));
    }

    @Test
    void createTasks() {
        fileManager.createTask("name", "description", Status.NEW,"14:09, 12.07.21", "20");
        fileManager.createTask("name", "description", Status.NEW,"14:09, 11.07.21", "20");
        final Task task = new Task("name", "description", Status.NEW, 0, "14:00, 12.07.21", "1");
        final Task task1 = new Task("name", "description", Status.NEW, 1, "14:00, 11.07.21", "1");

        assertEquals(2, fileManager.getListAllTasks().size());
        assertEquals(task, fileManager.getTask(0));
        assertEquals(task1, fileManager.getTask(1));
    }

    @Test
    void getEmptyListAllTasks() {
        assertEquals(0, fileManager.getListAllTasks().size());
    }

    @Test
    void createSubtaskWithoutEpic() {
        fileManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 12.07.21", "77");
        assertEquals(0, fileManager.getListAllSubtasks().size());
    }

    @Test
    void createFirstSubtask() {
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        fileManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 12.07.21", "77");

        assertEquals(1, fileManager.getListAllSubtasks().size());
    }

    @Test
    void createSubtasks() {
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 11.07.21", "77");
        fileManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 10.07.21", "77");
        fileManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 09.07.21", "77");

        assertEquals(2, fileManager.getListAllSubtasks().size());
    }

    @Test
    void getEpicWithExistingId() {
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");

        assertEquals(0, fileManager.getEpic(0).getId());
    }

    @Test
    void getEpicWithTwoElementsList() {
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");

        assertEquals(0, fileManager.getEpic(0).getId());
        assertEquals(1, fileManager.getEpic(1).getId());
    }

    @Test
    void createFirstEpic() {
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        final Epic epic = new Epic("name", "description", Status.NEW, 0, "14:00, 12.07.21", "1");

        assertEquals(1, fileManager.getListAllEpics().size());
        assertEquals(epic, fileManager.getEpic(0));
    }

    @Test
    void createEpics() {
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");

        final Epic epic = new Epic("name", "description", Status.NEW, 0, "14:00, 12.07.21", "1");
        final Epic epic1 = new Epic("name", "description", Status.NEW, 1, "14:00, 12.07.21", "1");

        assertEquals(2, fileManager.getListAllEpics().size());
        assertEquals(epic, fileManager.getEpic(0));
        assertEquals(epic1, fileManager.getEpic(1));
    }

    @Test
    void getAnyTaskByIdWithIdMissed() {
        assertNull(fileManager.getAnyTaskById(0));
    }

    @Test
    void getAnyTaskByIdWithExistingId() {
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77"); // id = 0
        fileManager.createTask("name", "description", Status.NEW,"14:09, 11.07.21", "20"); // id = 1
        fileManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 10.07.21", "77"); // id = 2

        assertEquals(0, fileManager.getAnyTaskById(0).getId());
        assertEquals(1, fileManager.getAnyTaskById(1).getId());
        assertEquals(2, fileManager.getAnyTaskById(2).getId());
    }

    @Test
    void getAnyTaskByIdWithNotEmptyList() {
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77"); // id = 0
        fileManager.createTask("name", "description", Status.NEW,"14:09, 11.07.21", "20"); // id = 1
        fileManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 10.07.21", "77"); // id = 2

        fileManager.createEpic("name", "description", Status.NEW, "14:09, 09.07.21", "77"); // id = 3
        fileManager.createTask("name", "description", Status.NEW,"14:09, 08.07.21", "20"); // id = 4
        fileManager.createSubtask("name", "description", Status.NEW, 3,"14:09, 07.07.21", "20"); // id = 5

        assertEquals(0, fileManager.getAnyTaskById(0).getId());
        assertEquals(1, fileManager.getAnyTaskById(1).getId());
        assertEquals(2, fileManager.getAnyTaskById(2).getId());
        assertEquals(3, fileManager.getAnyTaskById(3).getId());
        assertEquals(4, fileManager.getAnyTaskById(4).getId());
        assertEquals(5, fileManager.getAnyTaskById(5).getId());
    }

    @Test
    void deleteAllTasksWithEmptyList() {
        fileManager.deleteAllTasks();
        assertEquals(0, fileManager.getListAllTasks().size());
    }

    @Test
    void deleteAllTasksWithOneElementInList() {
        fileManager.createTask("name", "description", Status.NEW,"14:09, 12.07.21", "20");
        fileManager.deleteAllTasks();

        assertEquals(0, fileManager.getListAllTasks().size());
    }

    @Test
    void deleteAllTasksWithTwoElementsInList() {
        fileManager.createTask("name", "description", Status.NEW,"14:09, 11.07.21", "20");
        fileManager.createTask("name", "description", Status.NEW,"14:09, 10.07.21", "20");
        fileManager.deleteAllTasks();
        assertEquals(0, fileManager.getListAllTasks().size());
    }

    @Test
    void deleteAllSubtasksWithEmptyList() {
        fileManager.deleteAllSubtasks();
        assertEquals(0, fileManager.getListAllSubtasks().size());
    }

    @Test
    void deleteAllSubtasksWithOneElementInList() {
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        fileManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 12.07.21", "77");
        fileManager.deleteAllSubtasks();

        assertEquals(0, fileManager.getEpic(0).getSubtasksId().size());
        assertEquals(0, fileManager.getListAllSubtasks().size());
    }

    @Test
    void deleteAllSubtasksWithTwoElementInList() {
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 10.07.21", "77");
        fileManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 12.07.21", "77");
        fileManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 11.07.21", "77");
        fileManager.deleteAllSubtasks();

        assertEquals(0, fileManager.getEpic(0).getSubtasksId().size());
        assertEquals(0, fileManager.getListAllTasks().size());
    }

    @Test
    void deleteAllEpicsWithEmptyList() {
        fileManager.deleteAllEpics();
        assertEquals(0, fileManager.getListAllEpics().size());
    }

    @Test
    void deleteAllEpicsWithOneElementInList() {
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        fileManager.deleteAllEpics();

        assertEquals(0, fileManager.getListAllEpics().size());
    }

    @Test
    void deleteAllEpicsWithTwoElementInListAndWithSubtasks() {
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 11.07.21", "77");
        fileManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 10.07.21", "77");
        fileManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 09.07.21", "77");

        fileManager.deleteAllEpics();
        assertEquals(0, fileManager.getListAllEpics().size());
        assertEquals(0, fileManager.getListAllSubtasks().size());
    }

    @Test
    void removeTaskByIdWithMissedId() {
        fileManager.removeTaskById(0);
        assertEquals(0, fileManager.getListAllTasks().size());
    }

    @Test
    void removeTaskByIdWithOneElementInList() {
        fileManager.createTask("name", "description", Status.NEW,"14:09, 12.07.21", "20");
        fileManager.removeTaskById(0);
        assertEquals(0, fileManager.getListAllTasks().size());
    }

    @Test
    void removeTaskByIdWithTwoElementsInList() {
        fileManager.createTask("name", "description", Status.NEW,"14:09, 12.07.21", "20");
        fileManager.createTask("name", "description", Status.NEW,"14:09, 11.07.21", "20");
        fileManager.removeTaskById(0);
        fileManager.removeTaskById(1);
        assertEquals(0, fileManager.getListAllTasks().size());
    }

    @Test
    void removeTaskByIdWithTreeElementsInList() {
        fileManager.createTask("name", "description", Status.NEW,"14:09, 11.07.21", "20");
        fileManager.createTask("name", "description", Status.NEW,"14:09, 10.07.21", "20");
        fileManager.createTask("name", "description", Status.NEW,"14:09, 09.07.21", "20");
        fileManager.removeTaskById(1);

        assertEquals(2, fileManager.getListAllTasks().size());
    }

    @Test
    void removeSubtaskByIdWithMissedId() {
        fileManager.removeSubtaskById(0);
        assertEquals(0, fileManager.getListAllSubtasks().size());
    }

    @Test
    void removeSubtaskByIdWithOneElementInList() {
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        fileManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 12.07.21", "77");

        fileManager.removeSubtaskById(1);
        assertEquals(0, fileManager.getListAllSubtasks().size());
    }

    @Test
    void removeSubtaskByIdWithTwoElementsInList() {
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        fileManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 11.07.21", "77");
        fileManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 10.07.21", "77");

        fileManager.removeSubtaskById(1);
        fileManager.removeSubtaskById(2);

        assertEquals(0, fileManager.getListAllSubtasks().size());
    }

    @Test
    void removeSubtaskByIdWithTreeElementsInList() {
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        fileManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 11.07.21", "77");
        fileManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 10.07.21", "77");
        fileManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 09.07.21", "77");

        fileManager.removeSubtaskById(2);

        assertEquals(2, fileManager.getListAllSubtasks().size());
    }

    @Test
    void removeEpicByIdWithMissedId() {
        fileManager.removeEpicById(0);
        assertEquals(0, fileManager.getListAllEpics().size());
    }

    @Test
    void removeEpicByIdWithOneElementInList() {
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        fileManager.removeEpicById(0);
        assertEquals(0, fileManager.getListAllEpics().size());
    }

    @Test
    void removeEpicByIdWithTwoElementInList() {
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        fileManager.removeEpicById(0);
        fileManager.removeEpicById(1);
        assertEquals(0, fileManager.getListAllEpics().size());
    }

    @Test
    void removeEpicByIdWithTreeElementInList() {
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        fileManager.removeEpicById(1);

        assertEquals(2, fileManager.getListAllEpics().size());
    }

    @Test
    void removeNotEmptyEpicById() {
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 11.07.21", "77");
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 10.07.21", "77");

        fileManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 09.07.21", "77");
        fileManager.createSubtask("name", "description", Status.NEW, 1,"14:09, 08.07.21", "20");
        fileManager.createSubtask("name", "description", Status.NEW, 1,"14:09, 07.07.21", "20");

        fileManager.removeEpicById(0);
        fileManager.removeEpicById(1);
        fileManager.removeEpicById(2);

        assertEquals(0, fileManager.getListAllEpics().size());
        assertEquals(0, fileManager.getListAllSubtasks().size());
    }

    @Test
    void updateTaskMissedId() {
        fileManager.updateTask(0, new Task("name", "description", Status.NEW, 1, "14:00, 12.07.21", "1"));
        assertEquals(0, fileManager.getListAllTasks().size());
    }

    @Test
    void updateTaskWithEmptyList() {
        fileManager.createTask("name", "description", Status.NEW,"14:09, 12.07.21", "20");
        final Task task = new Task("name", "description", Status.NEW, 0, "14:00, 12.07.21", "1");

        fileManager.updateTask(0, new Task("name", "description", Status.NEW, 0, "14:09, 13.07.21", "77"));

        assertEquals(0, fileManager.getTask(0).getId());
        assertEquals(task, fileManager.getTask(0));
    }

    @Test
    void updateTaskWithNotEmptyList() {
        fileManager.createTask("name", "description", Status.NEW,"14:09, 12.07.21", "1");
        fileManager.createTask("name", "description", Status.IN_PROGRESS,"14:09, 11.07.21", "1");

        final Task task = new Task("name", "description", Status.NEW, 0, "14:00, 12.07.21", "1");
        final Task task1 = new Task("name", "description", Status.IN_PROGRESS, 1, "14:00, 11.07.21", "1");

        fileManager.updateTask(0, new Task("name", "description", Status.NEW, 0, "14:00, 12.07.21", "1"));
        fileManager.updateTask(1, new Task("name", "description", Status.IN_PROGRESS, 1, "14:00, 11.07.21", "1"));

        assertEquals(0, fileManager.getTask(0).getId());
        assertEquals(1, fileManager.getTask(1).getId());
        assertEquals(task, fileManager.getTask(0));
        assertEquals(task1, fileManager.getTask(1));
    }

    @Test
    void updateSubtaskMissedId() {
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        fileManager.updateSubtask(0, new Subtask("name", "description", Status.NEW, 1, 0, "14:00, 12.07.21", "1"));
        assertEquals(0, fileManager.getListAllSubtasks().size());
    }

    @Test
    void updateSubtaskWithEmptyList() {
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        fileManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 12.07.21", "77");
        final Subtask subtask = new Subtask("name", "description", Status.NEW, 1, 0, "14:00, 12.07.21", "1");

        fileManager.updateSubtask(1, new Subtask("name", "description", Status.NEW, 1, 0, "14:09, 13.07.21", "77"));

        assertEquals(1, fileManager.getSubtask(1).getId());
        assertEquals(subtask, fileManager.getSubtask(1));
    }

    @Test
    void updateSubtaskWithNotEmptyList() {
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "1");
        fileManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 11.07.21", "1");
        fileManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 10.07.21", "1");
        final Subtask subtask = new Subtask("name", "description", Status.NEW, 1, 0, "14:00, 11.07.21", "1");
        final Subtask subtask1 = new Subtask("name", "description", Status.NEW, 2, 0, "14:00, 10.07.21", "1");

        fileManager.updateSubtask(1, new Subtask("name", "description", Status.NEW, 1, 0, "14:00, 11.07.21", "1"));
        fileManager.updateSubtask(2, new Subtask("name", "description", Status.NEW, 2, 0, "14:00, 10.07.21", "1"));

        assertEquals(1, fileManager.getSubtask(1).getId());
        assertEquals(2, fileManager.getSubtask(2).getId());
        assertEquals(subtask, fileManager.getSubtask(1));
        assertEquals(subtask1, fileManager.getSubtask(2));
    }

    @Test
    void updateEpicMissedId() {
        fileManager.updateEpic(0, new Epic("name", "description", Status.NEW, 0, "14:00, 12.07.21", "1"));
        assertEquals(0, fileManager.getListAllEpics().size());
    }

    @Test
    void updateEpicWithEmptyList() {
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        final Epic epic = new Epic("name", "description", Status.NEW, 0, "14:00, 12.07.21", "1");

        fileManager.updateEpic(0, new Epic("name", "description", Status.NEW, 0, "14:09, 13.07.21", "77"));

        assertEquals(0, fileManager.getEpic(0).getId());
        assertEquals(epic, fileManager.getEpic(0));
    }

    @Test
    void updateEpicWithNotEmptyList() {
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");

        final Epic epic = new Epic("name", "description", Status.NEW, 0, "14:00, 12.07.21", "1");
        final Epic epic1 = new Epic("name", "description", Status.NEW, 1, "14:00, 12.07.21", "1");

        fileManager.updateEpic(0, new Epic("name", "description", Status.NEW, 0, "14:09, 13.07.21", "77"));
        fileManager.updateEpic(1, new Epic("name", "description", Status.NEW, 1, "14:09, 13.07.21", "77"));

        assertEquals(0, fileManager.getEpic(0).getId());
        assertEquals(1, fileManager.getEpic(1).getId());

        assertEquals(epic, fileManager.getEpic(0));
        assertEquals(epic1, fileManager.getEpic(1));
    }

    @Test
    void removeAnyTaskByIdWithOneElementInMap() {
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        fileManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 11.07.21", "77");
        fileManager.createTask("name", "description", Status.NEW,"14:09, 10.07.21", "20");

        fileManager.removeAnyTaskById(0);
        fileManager.removeAnyTaskById(1);
        fileManager.removeAnyTaskById(2);

        assertEquals(0,fileManager.getListAllTasks().size());
        assertEquals(0, fileManager.getListAllSubtasks().size());
        assertEquals(0, fileManager.getListAllEpics().size());
    }

    @Test
    void removeAnyTaskByIdWithMissedId() {
        fileManager.removeAnyTaskById(0);
        fileManager.removeAnyTaskById(1);
        fileManager.removeAnyTaskById(2);

        assertEquals(0,fileManager.getListAllTasks().size());
        assertEquals(0, fileManager.getListAllSubtasks().size());
        assertEquals(0, fileManager.getListAllEpics().size());
    }

    @Test
    void removeAnyTaskByIdWithTwoElementsInMap() {
        fileManager.createEpic("name", "description", Status.NEW, "14:09, 12.07.21", "77");
        fileManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 11.07.21", "77");
        fileManager.createTask("name", "description", Status.NEW,"14:09, 10.07.21", "20");

        fileManager.createEpic("name", "description", Status.NEW, "14:09, 09.07.21", "77");
        fileManager.createSubtask("name", "description", Status.NEW, 3, "14:09, 08.07.21", "77");
        fileManager.createTask("name", "description", Status.NEW,"14:09, 07.07.21", "20");

        fileManager.removeAnyTaskById(0);
        fileManager.removeAnyTaskById(1);
        fileManager.removeAnyTaskById(2);
        fileManager.removeAnyTaskById(3);
        fileManager.removeAnyTaskById(4);
        fileManager.removeAnyTaskById(5);

        assertEquals(0,fileManager.getListAllTasks().size());
        assertEquals(0, fileManager.getListAllSubtasks().size());
        assertEquals(0, fileManager.getListAllEpics().size());
    }

    @Test
    void calculateEpicStartTime() {
        fileManager.createEpic("name", "description", Status.NEW, "15:09, 12.07.21", "77");

        fileManager.createSubtask("name", "description", Status.NEW, 0, "14:00, 12.07.21", "1");
        fileManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 12.07.21", "1");

        fileManager.calculateEpicTime(0);
        assertEquals("2021-07-12T14:00", fileManager.getEpic(0).getStartTime().toString());
    }

    @Test
    void calculateEpicEndTime() {
        fileManager.createEpic("name", "description", Status.NEW, "15:09, 10.07.21", "77");

        fileManager.createSubtask("name", "description", Status.NEW, 0, "14:00, 12.07.21", "1");
        fileManager.createSubtask("name", "description", Status.NEW, 0, "14:09, 12.07.21", "1");

        fileManager.calculateEpicTime(0);
        assertEquals("2021-07-12T14:10", fileManager.getEpic(0).getEndTime().toString());
    }
}
