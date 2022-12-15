package Task12;

class Node<T> {
    private T value;
    private Node<T> nextNode;

    Node(T value) {
        this.value = value;
    }

    Node(T value, Node<T> node) {
        this.value = value;
        nextNode = node;
    }

    public T getValue() {
        return value;
    }
    public void setValue(T value) {
        this.value = value;
    }

    public boolean hasNext() {
        return nextNode != null;
    }

    public Node<T> getNext() {
        return nextNode;
    }

    public void setNext(Node<T> node) {
        nextNode = node;
    }
}
