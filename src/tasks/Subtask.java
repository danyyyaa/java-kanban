package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    final private Integer epicId;
    /*public Subtask(String name, String description, Status status, Integer id, Integer epicId) {
        super(name, description, status, id);
        this.epicId = epicId;
    }*/

    public Subtask(String name, String description, Status status, Integer id,
                   Integer epicId, String startDate, String duration) {
        super(name, description, status, id, startDate, duration);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Status status, Integer id, Integer epicId) {
        super(name, description, status, id);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "tasks.Subtask{" +
                "epicId=" + epicId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }

    public Integer getEpicId() {
        return epicId;
    }
}
