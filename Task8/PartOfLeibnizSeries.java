package Task8;

import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class PartOfLeibnizSeries implements Callable<Double> {
    private final int startIndex;
    private final int stepOverIndexes;
    private final CyclicBarrier barrier;
    private final AtomicBoolean isCanceled;
    private final AtomicLong theLargestNumberOfIterations;

    PartOfLeibnizSeries(int startIndex, int stepOverIndexes,
                        CyclicBarrier barrier,
                        AtomicBoolean isCanceled,
                        AtomicLong largestNumberOfIterations) {
        this.startIndex = startIndex;
        this.stepOverIndexes = stepOverIndexes;

        this.barrier = barrier;
        this.isCanceled = isCanceled;
        this.theLargestNumberOfIterations = largestNumberOfIterations;
    }

    @Override
    public Double call() {
        double sum = 0;
        int sign = (startIndex % 2 == 0) ? 1 : -1;
        double nIndex = startIndex;
        long iteration = Long.MIN_VALUE;

        // just calculation
        while (!isCanceled.get()) {
            sum = sum + sign * (4.0 / (2.0 * nIndex + 1.0));
            sign = (stepOverIndexes % 2 == 0) ? sign : -sign;
            nIndex += stepOverIndexes;

            if (iteration < Long.MAX_VALUE)
                iteration++;
        }

        // minimization of error
        // finding maximum of iterations
        synchronized (theLargestNumberOfIterations) {
            if (iteration > theLargestNumberOfIterations.get())
                theLargestNumberOfIterations.set(iteration);
        }

        // await while other threads check and/or set the largest number of iterations over all threads
        try {
            barrier.await();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // finishing calculation
        while (iteration < theLargestNumberOfIterations.get()) {
            sum = sum + sign * (4.0 / (2.0 * nIndex + 1.0));
            sign = (stepOverIndexes % 2 == 0) ? sign : -sign;
            nIndex += stepOverIndexes;
            iteration++;
        }

        return sum;
    }
}
