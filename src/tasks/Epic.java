package tasks;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    final private ArrayList<Integer> subtasksId;
    private LocalDateTime endTime;

    public Epic(String name, String description, Status status, Integer id,
                ArrayList<Integer> subtasksId, String startTime, String duration) {
        super(name, description, status, id, startTime, duration);
        this.subtasksId = subtasksId;
    }

    public Epic(String name, String description, Status status, Integer id, String startTime, String duration) {
        super(name, description, status, id, startTime, duration);
        this.subtasksId = new ArrayList<>();
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

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id + ", " +
                "subtasksId=" + subtasksId +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", endTime=" + endTime +
                '}';
    }
}
