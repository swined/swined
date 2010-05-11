package nsuj083;

import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {

    private final List<Runnable> tasks = new ArrayList<Runnable>();

    public synchronized void run(Runnable task) {
        tasks.add(task);
        notify();
    }

    public synchronized void run() {
        final String name = Thread.currentThread().getName();
        while (true) {
            try {
                wait();
                if (!tasks.isEmpty()) {
                    Runnable task = tasks.remove(0);
                    task.run();
                }
            } catch (Throwable e) {
                System.err.println("[srv] " + name + " : " + e);
            }
        }
    }

}
