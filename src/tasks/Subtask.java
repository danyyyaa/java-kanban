package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    final private Integer epicId;
    public Subtask(String name, String description, Status status, Integer id,
                   Integer epicId, String startTime, String duration) {
        super(name, description, status, id, startTime, duration);
        this.epicId = epicId;
        //this.startTime = LocalDateTime.parse(startTime, DATE_TIME_FORMATTER);
        //this.duration = Duration.ofMinutes(Long.parseLong(duration));
    }

    /*@Override
    public String toString() {
        return "tasks.Subtask{" +
                "epicId=" + epicId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }*/

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }
}
