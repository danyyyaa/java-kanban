import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    static final Integer HISTORY_LIMIT = 10;
    CustomLinkedList customLinkedList = new CustomLinkedList();

    @Override
    public void remove(int id) {
        if (customLinkedList.getNodeById(id).getData() instanceof Epic) {
            for (int subtaskId : ((Epic) customLinkedList.getNodeById(id).getData()).getSubtasksIdList()) {
                if (customLinkedList.idNodesMap.containsKey(subtaskId)) {
                    customLinkedList.removeNode((Node) customLinkedList.idNodesMap.get(subtaskId));
                }
            }
        }
        customLinkedList.removeNode((Node) customLinkedList.idNodesMap.get(id));
    }

    @Override
    public List<Task> getHistory() {
        return customLinkedList.getTask();
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            customLinkedList.linkLast(task);
        }
    }
}

class CustomLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    protected Map<Integer, Node> idNodesMap = new HashMap<>();

    void removeNode(Node del) {
        if (head == del) {
            head = del.next;
        }
        if (del.next != null) {
            del.next.prev = del.prev;
        }
        if (del.prev != null) {
            del.prev.next = del.next;
        }
        if (tail == del) {
            tail = del.prev;
        }

        for (int key : idNodesMap.keySet()) {
            if (idNodesMap.get(key).equals(del)) {
                idNodesMap.remove(key);
                break;
            }
        }
    }

    public void linkLast(Task task) {
        Node newNode = new Node<>(task);
        if (head == null) {
            head = tail = newNode;
            head.prev = null;
            tail.next = null;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
            tail.next = null;
        }
        idNodesMap.put(task.getId(), newNode);
    }

    List<Task> getTask() {
        List<Task> taskList = new ArrayList<>();

        Node current = head;
        for (int i = 0; (i < InMemoryHistoryManager.HISTORY_LIMIT) && (i < idNodesMap.size()); i++) {
            if (head == null) {
                return null;
            }
            System.out.println(current.getData().getId()); // удалить
            taskList.add(current.getData());
            current = current.next;
        }
        return taskList;
    }
    public Node getNodeById(int id) {
        return idNodesMap.get(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomLinkedList)) return false;
        CustomLinkedList<?> that = (CustomLinkedList<?>) o;
        return head.equals(that.head) && tail.equals(that.tail) && idNodesMap.equals(that.idNodesMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(head, tail, idNodesMap);
    }
}