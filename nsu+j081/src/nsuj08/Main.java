package nsuj08;

import java.util.Random;

public class Main {

    public static void main(String[] args) throws Throwable {
        int[] data = new int[100];
        for (int i = 0; i < data.length; i++) {
            data[i] = new Random().nextInt();
            System.out.println(data[i]);
        }
        for (int i = 0; i < 4; i++)
            new Thread(new SortThread(data)).start();
    }

}
