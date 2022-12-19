import Task12.MyList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MyListTests {
    @Test
    public void testing() {
        MyList<Integer> list = new MyList<>();

        Assertions.assertEquals(0, list.getSize());

        // ADD
        list.add(30);
        list.add(20);
        list.add(10);

        Assertions.assertEquals(3, list.getSize());

        Assertions.assertThrows(NullPointerException.class,
                () -> list.add(null), "item can not be null");

        // GET
        Assertions.assertEquals(30, list.get(0));
        Assertions.assertEquals(20, list.get(1));
        Assertions.assertEquals(10, list.get(2));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> list.get(-1), "index should be in range of [0, size)");
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> list.get(4), "index should be in range of [0, size)");

        // POP
        Assertions.assertEquals(30, list.pop(0));
        Assertions.assertEquals(2, list.getSize());

        Assertions.assertEquals(10, list.pop(1));
        Assertions.assertEquals(1, list.getSize());

        Assertions.assertEquals(20, list.pop(0));
        Assertions.assertEquals(0, list.getSize());

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> list.pop(-1), "index should be in range of [0, size)");
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> list.pop(0), "index should be in range of [0, size)");

        // SET
        list.add(50);
        list.add(40);
        list.add(10);

        Assertions.assertEquals(3, list.getSize());

        list.set(30, 2);

        Assertions.assertEquals(50, list.get(0));
        Assertions.assertEquals(40, list.get(1));
        Assertions.assertEquals(30, list.get(2));

        // SORT
        list.sort();

        Assertions.assertEquals(30, list.get(0));
        Assertions.assertEquals(40, list.get(1));
        Assertions.assertEquals(50, list.get(2));

        list.add(0);

        Assertions.assertEquals(4, list.getSize());
        Assertions.assertEquals(0, list.get(3));

        list.sort();

        Assertions.assertEquals(0, list.get(0));
        Assertions.assertEquals(30, list.get(1));
        Assertions.assertEquals(40, list.get(2));
        Assertions.assertEquals(50, list.get(3));
    }
}
