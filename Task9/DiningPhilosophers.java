package Task9;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophers {
    private final int philosophersNumber = 5;
    private final ExecutorService philosophers = Executors.newFixedThreadPool(philosophersNumber);
    private final AtomicBoolean isCanceled = new AtomicBoolean(false);

    DiningPhilosophers() {
        ArrayList<ReentrantLock> forks = new ArrayList<>();
        for (int i = 0; i < philosophersNumber; i++)
            forks.add(new ReentrantLock());

        for (int i = 0; i < philosophersNumber; i++)
            philosophers.submit(new Philosopher(i, forks, isCanceled));
    }

    public void CancelSimulation() {
        isCanceled.set(true);
        philosophers.shutdown();
    }
}
