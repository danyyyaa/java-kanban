package tasks;
import java.util.ArrayList;
import java.util.Collections;

public class Epic extends Task {
    final private ArrayList<Integer> subtasksId;

    public ArrayList<Integer> getSubtasksIdList() {
        return subtasksId;
    }

    public Epic(String name, String description, Status status, Integer id, ArrayList<Integer> subtasksId) {
        super(name, description, status, id);
        this.subtasksId = subtasksId;
    }

    public Epic(String name, String description, Status status, Integer id) {
        super(name, description, status, id);
        subtasksId = new ArrayList<>();
    }

    public String toString() {
        return "tasks.Epic{subtasksId=" + this.subtasksId + ", name='" + this.name + "', description='" + this.description + "', id=" + this.id + ", status='" + this.status + "'}";
    }

    public ArrayList<Integer> getSubtasksId() {
        return this.subtasksId;
    }

    public void addSubtaskId(int id) {
        subtasksId.add(id);
    }
}
