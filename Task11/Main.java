package Task11;

import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        Semaphore s1 = new Semaphore(1);
        Semaphore s2 = new Semaphore(1);

        try {
            s2.acquire();

            new Thread(new ChildThread(s1, s2)).start();

            for (int i = 0; i < 10; i++) {
                s1.acquire();

                System.out.println("Main " + i);

                s2.release();
            }
        }
        catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
        finally {
            s1.release();
            s2.release();
        }
    }
}
