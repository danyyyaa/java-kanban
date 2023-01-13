public class Subtask extends Task {
    private Integer epicId;
    public Subtask(String name, String description, Status status, Integer id, Integer epicId) {
        super(name, description, status, id);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
