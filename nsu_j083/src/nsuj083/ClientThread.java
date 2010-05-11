package nsuj083;

import java.util.Date;
import java.util.Random;

public class ClientThread implements Runnable {

    private final ISummator summator;

    public ClientThread(ISummator summator) {
        this.summator = summator;
    }

    public void run() {
        Random rand = new Random();
        try {
            int a = rand.nextInt();
            int b = rand.nextInt();
            System.out.println(new Date().toString() + " [cli/" + Thread.currentThread().getName() + "] sum(" + a + ", " + b + ")");
            int c = summator.sum(a, b);
            System.out.println(new Date().toString() + " [cli/" + Thread.currentThread().getName() + "] sum(" + a + ", " + b + ") = " + c);
        } catch (Throwable e) {
            System.err.println(new Date().toString() + " [cli/" + Thread.currentThread().getName() + "] " + e);
        }
    }
}
