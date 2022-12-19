package Task12;

public class MyList<T extends Comparable<T>> {
    private int size;
    private Node<T> head;

    public MyList() {}

    public MyList(Iterable<T> items) {
        Node<T> node = null;

        for (T item : items) {
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

    public int getSize() {
        return size;
    }

    public void add(T item) {
        if (item == null)
            throw new NullPointerException("item can not be null");

        if (head == null) {
            head = new Node<>(item);
        }
        else {
            Node<T> node = head;

            while (node.hasNext()) {
                node = node.getNext();
            }

            node.setNext(new Node<>(item));
        }
        size += 1;
    }

    public T get(int index) {
        if (index < 0 || index >= size)
            throw new IllegalArgumentException("index should be in range [0, size)");

        Node<T> node = head;

        for (int i = 0; i < index; i++) {
            node = node.getNext();
        }

        return node.getValue();
    }

    public T pop(int index) {
        if (index < 0 || index >= size)
            throw new IllegalArgumentException("index should be in range [0, size)");

        T item;

        if (index == 0) {
            item = head.getValue();
            head = head.hasNext() ? head.getNext() : null;
        }
        else {
            Node<T> node = head;
            Node<T> prev = head;

            for (int i = 0; i < index; i++) {
                prev = node;
                node = node.getNext();
            }
            prev.setNext(node.hasNext() ? node.getNext() : null);

            item = node.getValue();
        }
        size -= 1;

        return item;
    }

    public void set(T item, int index) {
        if (index < 0 || index >= size)
            throw new IllegalArgumentException("index should be in range [0, size)");

        Node<T> node = head;

        for (int i = 0; i < index; i++) {
            node = node.getNext();
        }

        node.setValue(item);
    }

    public void sort() {
        if (size < 1)
            return;

        Node<T> curr;
        Node<T> next;
        int lastIndex = size;
        boolean swapped = true;

        while (swapped) {
            swapped = false;
            curr = head;

            for (int i = 0; i < lastIndex - 1; i++) {
                if (curr.hasNext()) {
                    next = curr.getNext();

                    if (curr.getValue().compareTo(next.getValue()) > 0) {
                        T tmp = curr.getValue();
                        curr.setValue(next.getValue());
                        next.setValue(tmp);
                        swapped = true;
                    }

                    curr = next;
                }
            }
            lastIndex -= 1;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("");

        if (head != null) {
            Node<T> node = head;

            while (node.hasNext()) {
                builder.append(node.getValue()).append(" ");
                node = node.getNext();
            }
            builder.append(node.getValue());
        }

        return builder.toString();
    }
}
