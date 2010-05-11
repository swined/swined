package nsuj083;

import java.util.Date;

public class Summator implements ISummator {

    public int sum(int a, int b) {
        System.out.println(new Date().toString() + " [sum/" + Thread.currentThread().getName()
                + "] sum(" + a + ", " + b + ")");
        return a + b;
    }
}
