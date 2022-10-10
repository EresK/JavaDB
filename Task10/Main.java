package Task10;

import java.util.concurrent.locks.ReentrantLock;

public class Main {

    public static void main(String[] args) {
        ReentrantLock lock1 = new ReentrantLock();
        ReentrantLock lock2 = new ReentrantLock();
        ReentrantLock lock3 = new ReentrantLock();

        try {
            lock2.lock();

            new Thread(new ChildThread(lock1, lock2, lock3)).start();

            while (!lock3.isLocked()) {
                Thread.yield();
            }

            for (int i = 0; i < 10; i++) {
                lock1.lock();
                lock2.unlock();

                System.out.println("Main " + i);

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
