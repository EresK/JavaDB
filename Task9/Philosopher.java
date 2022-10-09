package Task9;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class Philosopher implements Runnable {
    private final int philosopherID;
    private final ArrayList<ReentrantLock> forks;
    private final AtomicBoolean isCanceled;

    Philosopher(int philosopherID, ArrayList<ReentrantLock> forks, AtomicBoolean isCanceled) {
        this.philosopherID = philosopherID;
        this.forks = forks;
        this.isCanceled = isCanceled;
    }

    @Override
    public void run() {
        Random random = new Random();
        ReentrantLock forkOne, forkTwo;

        while (!isCanceled.get()) {
            // thinking
            try {
                System.out.println("Philosopher " + philosopherID + " is thinking");
                Thread.sleep(random.nextInt(10, 500));
            }
            catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }

            // having a dinner
            // if philosopherID != 0 then forkOne is left fork and forkTwo is right
            // else vise versa
            forkOne = (philosopherID == 0) ? forks.get(philosopherID) : forks.get(philosopherID - 1);
            forkTwo = (philosopherID == 0) ? forks.get(forks.size() - 1) : forks.get(philosopherID);

            try {
                forkOne.lock();
                forkTwo.lock();

                System.out.println("Philosopher " + philosopherID + " having dinner");
                Thread.sleep(random.nextInt(10, 500));
            }
            catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
            finally {
                if (forkOne.isHeldByCurrentThread())
                    forkOne.unlock();
                if (forkTwo.isHeldByCurrentThread())
                    forkTwo.unlock();

                System.out.println("Philosopher " + philosopherID + " finished");
            }
        }
    }
}
