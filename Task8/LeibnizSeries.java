package Task8;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class LeibnizSeries {
    private final int threadsNumber;
    private final ExecutorService service;
    private final ArrayList<Future<Double>> futures;

    private final CyclicBarrier barrier;
    private final AtomicBoolean isCanceled = new AtomicBoolean(false);
    private final AtomicReference<Double> theLargestNumberOfIterationsOfThread;

    LeibnizSeries(int threadsNumber) {
        this.threadsNumber = threadsNumber > 0 ? threadsNumber : 2;
        service = Executors.newFixedThreadPool(threadsNumber);

        futures = new ArrayList<>();
        barrier = new CyclicBarrier(threadsNumber);
        theLargestNumberOfIterationsOfThread = new AtomicReference<>(0.0);
    }

    public void Pi() {
        for (int i = 0; i < threadsNumber; i++) {
            futures.add(service.submit(
                    new PartOfLeibnizSeries(i, threadsNumber,
                            barrier, isCanceled, theLargestNumberOfIterationsOfThread)
            ));
        }
    }

    public double CancelCalculation() {
        isCanceled.set(true);

        double sum = 0;

        try {
            for (var f : futures)
                sum += f.get();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            service.shutdown();
        }

        return sum;
    }
}
