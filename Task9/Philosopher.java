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
                System.out.println("Philosopher: " + philosopherID + " is thinking");

                Thread.sleep(random.nextInt(10));
            }
            catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }

            // eating
            // if philosopherID != 0 then forkOne is left fork and forkTwo is right
            // else vise versa
            forkOne = (philosopherID == 0) ? forks.get(philosopherID) : forks.get(philosopherID - 1);
            forkTwo = (philosopherID == 0) ? forks.get(forks.size() - 1) : forks.get(philosopherID);

            int debugForkOneID = (philosopherID == 0) ? philosopherID : philosopherID - 1;
            int debugForkTwoID = (philosopherID == 0) ? forks.size() - 1 : philosopherID;

            System.out.println("Philosopher: " + philosopherID + " try to get forks");

            try {
                forkOne.lock();
                forkTwo.lock();

                System.out.println("Philosopher: " + philosopherID + " is eating, " +
                        debugForkOneID + " " + debugForkTwoID);

                Thread.sleep(random.nextInt(2000));
            }
            catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
            finally {
                if (forkOne.isHeldByCurrentThread())
                    forkOne.unlock();
                if (forkTwo.isHeldByCurrentThread())
                    forkTwo.unlock();
            }

            System.out.println("Philosopher: " + philosopherID + " finished");
        }
    }
}
