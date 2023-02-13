package tasks;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Epic extends Task {
    final private ArrayList<Integer> subtasksId;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasksId, epic.subtasksId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subtasksId);
    }
}
