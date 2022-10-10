package Task11;

import java.util.concurrent.Semaphore;

public class ChildThread implements Runnable {
    private final Semaphore s1;
    private final Semaphore s2;

    ChildThread(Semaphore s1, Semaphore s2) {
        this.s1 = s1;
        this.s2 = s2;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                s2.acquire();

                System.out.println("Child " + i);

                s1.release();
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
