package managers.history;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest extends InMemoryHistoryManager {
    private HistoryManager historyManager;

    @BeforeEach
    public void create() {
        historyManager = new InMemoryHistoryManager(){};
    }

    @Test
    void addInHistoryWithEmptyList() {
        historyManager.add(new Task("testTask", "description", Status.NEW, 1));
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void addInHistoryWithNotEmptyList() {
        historyManager.add(new Task("testTask", "description", Status.NEW, 1));
        historyManager.add(new Task("testTask", "description", Status.NEW, 2));
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(2, history.size(), "История не пустая.");
    }

    @Test
    void addDuplicate() {
        historyManager.add(new Task("testTask", "description", Status.NEW, 1));
        historyManager.add(new Task("testTask", "description", Status.NEW, 1));
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void getEmptyHistory() {
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(0, history.size());
    }

    @Test
    void getNotEmptyHistory() {
        historyManager.add(new Task("testTask", "description", Status.NEW, 1));
        final List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());

    }

    @Test
    void removeFirstElement() {
        historyManager.add(new Task("testTask", "description", Status.NEW, 1));
        historyManager.add(new Task("testTask", "description", Status.NEW, 2));
        historyManager.remove(1);
        final List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
    }

    @Test
    void removeLastElement() {
        historyManager.add(new Task("testTask", "description", Status.NEW, 1));
        historyManager.add(new Task("testTask", "description", Status.NEW, 2));
        historyManager.remove(2);
        final List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
    }

    @Test
    void removeMiddleElement() {
        historyManager.add(new Task("testTask", "description", Status.NEW, 1));
        historyManager.add(new Task("testTask", "description", Status.NEW, 2));
        historyManager.add(new Task("testTask", "description", Status.NEW, 3));
        historyManager.remove(2);
        final List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());

    }
}