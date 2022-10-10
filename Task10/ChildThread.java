package Task10;

import java.util.concurrent.locks.ReentrantLock;

public class ChildThread implements Runnable {
    private final ReentrantLock lock1;
    private final ReentrantLock lock2;
    private final ReentrantLock lock3;

    ChildThread(ReentrantLock lock1, ReentrantLock lock2, ReentrantLock lock3) {
        this.lock1 = lock1;
        this.lock2 = lock2;
        this.lock3 = lock3;
    }

    @Override
    public void run() {
        try {
            lock3.lock();
            lock2.lock();
            lock3.unlock();
            for (int i = 0; i < 10; i++) {
                lock1.lock();
                lock2.unlock();

                System.out.println("Child " + i);

                lock3.lock();
                lock1.unlock();
                lock2.lock();
                lock3.unlock();
            }
        } finally {
            if (lock1.isHeldByCurrentThread()) lock1.unlock();
            if (lock2.isHeldByCurrentThread()) lock2.unlock();
            if (lock3.isHeldByCurrentThread()) lock3.unlock();
        }
    }
}
