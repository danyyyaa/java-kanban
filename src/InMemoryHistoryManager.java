
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    static final Integer HISTORY_LIMIT = 10;
    public static List<Task> historyList;

    public InMemoryHistoryManager() {
        historyList = new ArrayList<>();
    }

    @Override
    public List<Task> getHistory() {
        if (historyList.size() > HISTORY_LIMIT) {
            while (historyList.size() > HISTORY_LIMIT) {
                historyList.remove(historyList.size() - 1);
            }
        }
        return historyList;
    }

    @Override
    public void add(Task task) {
        historyList.add(task);
    }
}

