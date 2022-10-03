package Task8;

import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class LeibnizSeriesBarrier {
    private final int threads_number;
    private final AtomicBoolean isCanceled = new AtomicBoolean(false);
    private final ExecutorService service;
    private final ArrayList<Future<Double>> futures;

    LeibnizSeriesBarrier(int threads_number) {
        this.threads_number = threads_number > 0 ? threads_number : 2;

        service = Executors.newFixedThreadPool(threads_number);
        futures = new ArrayList<>();
    }

    public void Pi() {
        for (int i = 0; i < threads_number; i++) {
            futures.add(service.submit(new PartOfLeibnizSeries(i, threads_number, isCanceled)));
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
