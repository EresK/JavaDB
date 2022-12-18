import Task12.MyVector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MyVectorTests {
    @Test
    public void testing() {
        MyVector<Integer> vector = new MyVector<>();

        Assertions.assertEquals(0, vector.getLength());
        Assertions.assertEquals(1, vector.getCapacity());

        // ADD
        vector.add(30);
        vector.add(20);

        Assertions.assertEquals(2, vector.getCapacity());

        vector.add(10);

        Assertions.assertEquals(3, vector.getLength());
        Assertions.assertEquals(4, vector.getCapacity());

        // GET
        Assertions.assertEquals(30, vector.get(0));
        Assertions.assertEquals(20, vector.get(1));
        Assertions.assertEquals(10, vector.get(2));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> vector.get(-1), "index should be in range of [0, length)");
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> vector.get(4), "index should be in range of [0, length)");

        // SET
        vector.set(50, 0);
        vector.set(40, 1);
        vector.set(30, 2);

        Assertions.assertEquals(50, vector.get(0));
        Assertions.assertEquals(40, vector.get(1));
        Assertions.assertEquals(30, vector.get(2));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> vector.set(100, -1), "index should be in range of [0, length)");
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> vector.set(100, 4), "index should be in range of [0, length)");

        // SORT
        vector.add(20);
        vector.add(10);
        vector.add(0);

        Assertions.assertEquals(6, vector.getLength());
        Assertions.assertEquals(8, vector.getCapacity());

        vector.sort();

        Assertions.assertEquals(0, vector.get(0));
        Assertions.assertEquals(10, vector.get(1));
        Assertions.assertEquals(20, vector.get(2));
        Assertions.assertEquals(30, vector.get(3));
        Assertions.assertEquals(40, vector.get(4));
        Assertions.assertEquals(50, vector.get(5));
    }
}
