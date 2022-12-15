package Task12;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MyLinkedList<String> list = new MyLinkedList<>();

        ChildThread childThread = new ChildThread(list, new AtomicBoolean(false));
        new Thread(childThread).start();

        boolean continueAdd = true;
        while (continueAdd) {
            String line = scanner.nextLine();

            switch (line) {
                case "":
                    printList(list);
                    break;
                case "--exit":
                    printList(list);
                    continueAdd = false;
                    break;
                default:
                    addToList(list, line);
            }
        }

        childThread.cancel();
    }

    private synchronized static void printList(MyLinkedList<String> list) {
        System.out.println(list);
    }

    private synchronized static void addToList(MyLinkedList<String> list, String line) {
        list.add(line);
    }
}
