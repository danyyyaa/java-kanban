import tasks.Task;

public class Node<E> {
    protected Node<E> next;
    protected Node<E> prev;
    final private Task data;

    public Node(Task data) {
        this.data = data;
    }
    public Task getData() {
        return this.data;
    }
}
