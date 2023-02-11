package managers.history;

public class Node<E> {
    protected Node<E> next;
    protected Node<E> prev;
    protected E data;

    public Node(E data) {
        this.data = data;
    }
    public E getData() {
        return this.data;
    }
}
