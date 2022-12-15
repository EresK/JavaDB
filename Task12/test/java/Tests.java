import Task12.ChildThread;
import Task12.MyLinkedList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Tests {
    @Test
    public void TestAdd() {
        MyLinkedList<Integer> list = new MyLinkedList<>();

        Assertions.assertEquals(0, list.getSize());

        list.add(1);
        Assertions.assertEquals(1, list.getSize());

        list.add(2);
        list.add(3);
        Assertions.assertEquals(3, list.getSize());

        list = new MyLinkedList<>(List.of(1, 2, 3, 4, 5));
        Assertions.assertEquals(5, list.getSize());
    }

    @Test
    public void TestPopGet() {
        MyLinkedList<Integer> list = new MyLinkedList<>();

        list.add(1);
        list.add(2);
        list.add(3);

        int value = list.get();
        Assertions.assertEquals(3, value);
        Assertions.assertEquals(3, list.getSize());

        value = list.pop();
        Assertions.assertEquals(3, value);
        Assertions.assertEquals(2, list.getSize());

        value = list.pop();
        Assertions.assertEquals(2, value);
        Assertions.assertEquals(1, list.getSize());

        value = list.pop();
        Assertions.assertEquals(1, value);
        Assertions.assertEquals(0, list.getSize());

        Assertions.assertThrows(IllegalStateException.class, list::get, "List is empty");
        Assertions.assertThrows(IllegalStateException.class, list::pop, "List is empty");
    }

    @Test
    public void TestPopGetWithIndex() {
        MyLinkedList<Integer> list = new MyLinkedList<>();

        list.add(1);
        list.add(2);
        list.add(3);

        Assertions.assertThrows(IllegalArgumentException.class, () -> list.pop(-1),
                "index should be in range [0 to size) and size > 0");
        Assertions.assertThrows(IllegalArgumentException.class, () -> list.get(3),
                "index should be in range [0 to size) and size > 0");

        int value = list.get(0);
        Assertions.assertEquals(1, value);
        Assertions.assertEquals(3, list.getSize());

        value = list.pop(0);
        Assertions.assertEquals(1, value);
        Assertions.assertEquals(2, list.getSize());

        value = list.get(0);
        Assertions.assertEquals(2, value);

        value = list.get(1);
        Assertions.assertEquals(3, value);
        Assertions.assertEquals(2, list.getSize());

        value = list.pop(1);
        Assertions.assertEquals(3, value);

        value = list.pop(0);
        Assertions.assertEquals(2, value);
        Assertions.assertEquals(0, list.getSize());

        Assertions.assertThrows(IllegalArgumentException.class, () -> list.pop(0),
            "index should be in range [0 to size) and size > 0");
    }

    @Test
    public void TestSet() {
        MyLinkedList<Integer> list = new MyLinkedList<>();

        list.add(1);
        list.add(2);
        list.add(3);

        list.set(0, 10);

        int value = list.get(0);
        Assertions.assertEquals(10, value);

        list.set(1, 20);
        list.set(2, 30);

        value = list.get(1);
        Assertions.assertEquals(20, value);

        value = list.get(2);
        Assertions.assertEquals(30, value);

        Assertions.assertEquals(3, list.getSize());

        Assertions.assertThrows(IllegalArgumentException.class, () -> list.pop(-1),
                "index should be in range [0 to size) and size > 0");

        Assertions.assertThrows(IllegalArgumentException.class, () -> list.pop(100),
                "index should be in range [0 to size) and size > 0");
    }

    @Test
    public void Sorting() {
        MyLinkedList<String> list = new MyLinkedList<String>(List.of("3", "2", "1"));

        new ChildThread(list, new AtomicBoolean(false)).sort(list);

        String value = list.get(0);
        Assertions.assertEquals("1", value);

        value = list.get(1);
        Assertions.assertEquals("2", value);

        value = list.get(2);
        Assertions.assertEquals("3", value);

        list = new MyLinkedList<String>(List.of("c", "b", "a"));
        new ChildThread(list, new AtomicBoolean(false)).sort(list);

        value = list.get(0);
        Assertions.assertEquals("a", value);

        value = list.get(1);
        Assertions.assertEquals("b", value);

        value = list.get(2);
        Assertions.assertEquals("c", value);
    }
}
