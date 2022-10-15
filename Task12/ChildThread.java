package Task12;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChildThread implements Runnable {
    private final List<String> list;
    private final AtomicBoolean isCanceled;

    ChildThread(List<String> list, AtomicBoolean isCanceled) {
        this.list = list;
        this.isCanceled = isCanceled;
    }

    @Override
    public void run() {
        while (!isCanceled.get()) {
            try {
                Thread.sleep(5000);
                sort(list);
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

    private synchronized void sort(List<String> list) {
        synchronized (list) {
            boolean wasSwapped = true;
            int lastIndex = list.size();

            while (wasSwapped) {
                wasSwapped = false;
                for (int i = 0; i < lastIndex - 1; i++) {
                    if (list.get(i).compareTo(list.get(i + 1)) > 0) {
                        String leftTmp = list.get(i);
                        list.set(i, list.get(i + 1));
                        list.set(i + 1, leftTmp);
                        wasSwapped = true;
                    }
                }
                lastIndex -= 1;
            }
        }
    }
}
