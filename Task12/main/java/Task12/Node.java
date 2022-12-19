package Task12;

public class Node<T> {
    private T value;
    private Node<T> nextNode;

    public Node(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Node<T> getNext() {
        return nextNode;
    }

    public void setNext(Node<T> node) {
        nextNode = node;
    }

    public boolean hasNext() {
        return nextNode != null;
    }
}
