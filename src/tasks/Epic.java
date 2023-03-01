package tasks;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    final private ArrayList<Integer> subtasksId;
    Status status;


    public Epic(String name, String description, Status status, Integer id,
                ArrayList<Integer> subtasksId, String startDate, String duration) {
        super(name, description, status, id, startDate, duration);
        this.subtasksId = subtasksId;
        this.status = status;
    }

    public Epic(String name, String description, Status status, Integer id, String startDate, String duration) {
        super(name, description, status, id, startDate, duration);
        subtasksId = new ArrayList<>();
        this.status = status;
    }

    public Epic(String name, String description, Status status, Integer id, ArrayList<Integer> subtasksId) {
        super(name, description, status, id);
        this.subtasksId = subtasksId;
        this.status = status;
    }

    public Epic(String name, String description, Status status, Integer id) {
        super(name, description, status, id);
        subtasksId = new ArrayList<>();
        this.status = status;
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

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
    }
}
