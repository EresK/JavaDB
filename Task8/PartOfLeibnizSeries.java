package Task8;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

public class PartOfLeibnizSeries implements Callable<Double> {
    private final int startIndex;
    private final int stepOverIndexes;

    private final AtomicBoolean isCanceled;


    PartOfLeibnizSeries(int startIndex, int stepOverIndexes, AtomicBoolean isCanceled) {
        this.startIndex = startIndex;
        this.stepOverIndexes = stepOverIndexes;

        this.isCanceled = isCanceled;
    }

    @Override
    public Double call() {
        double sum = 0;
        double sign = (startIndex % 2 == 0) ? 1 : -1;
        double nIndex = startIndex;

        while (!isCanceled.get()) {
            sum = sum + sign * (4.0 / (2.0 * nIndex + 1.0));
            nIndex += stepOverIndexes;
        }

        return sum;
    }
}
