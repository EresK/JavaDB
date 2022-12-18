package Task12;

import java.util.Arrays;

public class MyVector<T extends Comparable<T>> {
    private int length;
    private int capacity;
    private Object[] values;

    public MyVector() {
        values = new Object[1];
        length = 0;
        capacity = 1;
    }

    public int getLength() {
        return length;
    }

    public int getCapacity() {
        return capacity;
    }

    public void add(T item) {
        if (length >= capacity) {
            resize();
        }
        values[length++] = item;
    }

    public T get(int index) {
        if (index < 0 || index >= length)
            throw new IllegalArgumentException("index should be in range of [0, length)");

        return (T) values[index];
    }

    public void set(T item, int index) {
        if (index < 0 || index >= length)
            throw new IllegalArgumentException("index should be in range of [0, length)");

        values[index] = item;
    }

    public void sort() {
        boolean swapped = true;
        int lastIndex = length;

        while (swapped) {
            swapped = false;
            for (int i = 0; i < lastIndex - 1; i++) {
                T curr = (T) values[i];
                T next = (T) values[i + 1];

                if (curr.compareTo(next) > 0) {
                    values[i] = next;
                    values[i + 1] = curr;
                    swapped = true;
                }
            }
            lastIndex -= 1;
        }
    }

    private void resize() {
        if (capacity < capacity * 2) {
            capacity = capacity * 2;
            values = Arrays.copyOf(values, capacity);
        }
        else {
            throw new ArrayStoreException("Can not allocate memory");
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("");

        for (Object item: values) {
            if (item != null) {
                builder.append((T) item);
                builder.append(" ");
            }
            else {
                break;
            }
        }

        return builder.toString();
    }
}
