package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected Integer id;
    protected Status status;

    protected LocalDateTime startTime;
    protected Duration duration;
    protected static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm, dd.MM.yy");

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public Status getStatus() {
        return status;
    }



    public Task(String name, String description, Status status, Integer id, String startTime, String duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.startTime = LocalDateTime.parse(startTime, DATE_TIME_FORMATTER);
        this.duration = Duration.ofMinutes(Long.parseLong(duration));
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description)
                && Objects.equals(id, task.id) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public String getStringDuration() {
        return duration.toString();
    }

    /*public void setDuration(String duration) {
        this.duration = Duration.parse(duration);
    }*/

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }
}
