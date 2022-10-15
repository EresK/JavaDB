package Task12;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        ArrayList<String> list = new ArrayList<>();

        ChildThread childThread = new ChildThread(list, new AtomicBoolean(false));
        new Thread(childThread).start();

        while (true) {
            String line = scanner.nextLine();

            if (line.equals("")) {
                System.out.println(list);
            }
            else if (line.equals("/exit")) {
                System.out.println(list);
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
