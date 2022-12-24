package Task12;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyList<T extends Comparable<T>> {
    private int size;
    private Node<T> head;
    private final Lock mutex = new ReentrantLock();

    public MyList() {
    }

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

        mutex.lock();
        try {
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
        finally {
            mutex.unlock();
        }
    }

    public T get(int index) {
        if (index < 0 || index >= size)
            throw new IllegalArgumentException("index should be in range [0, size)");

        mutex.lock();
        try {
            Node<T> node = head;

            for (int i = 0; i < index; i++)
                node = node.getNext();

            return node.getValue();
        }
        finally {
            mutex.unlock();
        }
    }

    public T pop(int index) {
        if (index < 0 || index >= size)
            throw new IllegalArgumentException("index should be in range [0, size)");

        mutex.lock();
        try {
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
        finally {
            mutex.unlock();
        }
    }

    public void set(T item, int index) {
        if (index < 0 || index >= size)
            throw new IllegalArgumentException("index should be in range [0, size)");

        mutex.lock();
        try {
            Node<T> node = head;

            for (int i = 0; i < index; i++)
                node = node.getNext();

            node.setValue(item);
        }
        finally {
            mutex.unlock();
        }
    }

    public void sort() {
        if (size < 1)
            return;

        mutex.lock();
        try {
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
        finally {
            mutex.unlock();
        }
    }

    @Override
    public String toString() {
        mutex.lock();
        try {
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
        finally {
            mutex.unlock();
        }
    }
}
