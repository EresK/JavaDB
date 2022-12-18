package Task12;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MyVector<String> vector = new MyVector<>();

        ChildThread childThread = new ChildThread(vector, new AtomicBoolean(false));
        new Thread(childThread).start();

        boolean continueAdd = true;

        while (continueAdd) {
            String line = scanner.nextLine();

            switch (line) {
                case "":
                    printVector(vector);
                    break;
                case "--exit":
                    printVector(vector);
                    continueAdd = false;
                    break;
                default:
                    addToVector(vector, line);
            }
        }

        childThread.cancel();
    }

    private synchronized static void printVector(MyVector<String> vector) {
        System.out.println(vector);
    }

    private synchronized static void addToVector(MyVector<String> vector, String line) {
        vector.add(line);
    }
}
