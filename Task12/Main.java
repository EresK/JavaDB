package Task12;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> list = new LinkedList<>();

        ChildThread childThread = new ChildThread(list, new AtomicBoolean(false));
        new Thread(childThread).start();

        while (true) {
            String line = scanner.nextLine();

            if (line.equals("")) {
                synchronized (list) {
                    System.out.println(list);
                }
            }
            else if (line.equals("/exit")) {
                synchronized (list) {
                    System.out.println(list);
                }
                childThread.cancel();
                break;
            }
            else {
                synchronized (list) {
                    list.add(line);
                }
            }
        }
    }
}
