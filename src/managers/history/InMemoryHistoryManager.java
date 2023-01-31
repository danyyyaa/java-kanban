package managers.history;

import tasks.Epic;
import tasks.Task;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class InMemoryHistoryManager implements HistoryManager {
    static final Integer HISTORY_LIMIT = 10;
    CustomLinkedList<Task> customLinkedList = new CustomLinkedList<>();

    @Override
    public void remove(int id) {
        if (customLinkedList.getNodeById(id).getData() instanceof Epic) {
            for (int subtaskId : ((Epic) customLinkedList.getNodeById(id).getData()).getSubtasksIdList()) {
                if (customLinkedList.idNodesMap.containsKey(subtaskId)) {
                    customLinkedList.removeNode(customLinkedList.idNodesMap.get(subtaskId));
                }
            }
        }
        customLinkedList.removeNode(customLinkedList.idNodesMap.get(id));
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
    protected Map<Integer, Node<T>> idNodesMap = new HashMap<>();

    void removeNode(Node<T> del) {
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
        Node<T> newNode = (Node<T>) new Node<>(task);
        if (head == null) {
            head = tail = newNode;
            head.prev = null;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        tail.next = null;
        idNodesMap.put(task.getId(), newNode);
    }

    List<Task> getTask() {
        List<Task> taskList = new ArrayList<>();

        Node<T> current = head;
        for (int i = 0; (i < InMemoryHistoryManager.HISTORY_LIMIT) && (i < idNodesMap.size()); i++) {
            if (head == null) {
                return null;
            }
            taskList.add((Task) current.getData());
            current = current.next;
        }
        return taskList;
    }
    public Node<T> getNodeById(int id) {
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