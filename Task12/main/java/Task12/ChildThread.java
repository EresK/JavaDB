package Task12;

import java.util.concurrent.atomic.AtomicBoolean;

public class ChildThread implements Runnable {
    private final MyList<String> list;
    private final AtomicBoolean isCanceled;

    public ChildThread(MyList<String> list, AtomicBoolean isCanceled) {
        this.list = list;
        this.isCanceled = isCanceled;
    }

    @Override
    public void run() {
        while (!isCanceled.get()) {
            try {
                Thread.sleep(5000);
                list.sort();
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
