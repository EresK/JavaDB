package Task12;

import java.util.concurrent.atomic.AtomicBoolean;

public class ChildThread implements Runnable {
    private final MyVector<String> vector;
    private final AtomicBoolean isCanceled;

    public ChildThread(MyVector<String> vector, AtomicBoolean isCanceled) {
        this.vector = vector;
        this.isCanceled = isCanceled;
    }

    @Override
    public void run() {
        while (!isCanceled.get()) {
            try {
                Thread.sleep(5000);
                vector.sort();
            }
            catch (InterruptedException e) {
                System.err.println(e.getMessage());
                break;
            }
        }
    }

    public void cancel() {
        isCanceled.set(true);
    }
}
