package Task12;

public class MyLinkedList<T> {
    private Node<T> head;
    private int size;

    public MyLinkedList() {}

    public MyLinkedList(Iterable<T> items) {
        Node<T> node = null;

        for (T item: items) {
            if (node == null) {
                head = new Node<>(item);
                node = head;
            }
            else {
                node.setNext(new Node<>(item));
                node = node.getNext();
            }
            size += 1;
        }
    }

    public void add(T item) {
        if (head == null)
            head = new Node<>(item);
        else {
            Node<T> node = head;

            while (node.hasNext()) {
                node = node.getNext();
            }

            node.setNext(new Node<>(item));
        }

        size += 1;
    }

    public T pop() {
        if (head == null)
            throw new IllegalStateException("List is empty");

        Node<T> node = head;
        Node<T> prev = head;

        while (node.hasNext()) {
            prev = node;
            node = node.getNext();
        }
        prev.setNext(null);

        if (node == head)
            head = null;

        size -= 1;

        return node.getValue();
    }

    public T pop(int index) {
        if (index < 0 || size < 1 || index >= size)
            throw new IllegalArgumentException("index should be in range [0 to size) and size > 0");

        Node<T> node = head;
        Node<T> prev = head;

        int i = 0;
        while (i < index) {
            if (node.hasNext()) {
                prev = node;
                node = node.getNext();
            }
            else {
                throw new IllegalStateException("Mismatch between size and nodes count");
            }
            i += 1;
        }
        prev.setNext(node.getNext());

        if (node == head) {
            if (head.hasNext())
                head = head.getNext();
            else
                head = null;
        }

        size -= 1;

        return node.getValue();
    }

    public T get() {
        if (head == null)
            throw new IllegalStateException("List is empty");

        Node<T> node = head;

        while (node.hasNext()) {
            node = node.getNext();
        }

        return node.getValue();
    }

    public T get(int index) {
        if (index < 0 || size < 1 || index >= size)
            throw new IllegalArgumentException("index should be in range [0 to size) and size > 0");

        Node<T> node = head;

        int i = 0;
        while (i < index) {
            if (node.hasNext())
                node = node.getNext();
            else
                throw new IllegalStateException("Mismatch between size and nodes count");
            i += 1;
        }

        return node.getValue();
    }

    public void set(int index, T item) {
        if (index < 0 || size < 1 || index >= size)
            throw new IllegalArgumentException("index should be in range [0 to size) and size > 0");

        Node<T> node = head;

        int i = 0;
        while (i < index) {
            if (node.hasNext())
                node = node.getNext();
            else
                throw new IllegalStateException("Mismatch between size and nodes count");
            i += 1;
        }

        node.setValue(item);
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("");

        Node<T> node = head;

        while (node.hasNext()) {
            builder.append(node.getValue()).append(", ");
            node = node.getNext();
        }
        builder.append(node.getValue());

        return builder.toString();
    }
}
