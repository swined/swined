package nsu_j082;

import java.util.Random;

public class Main {

    public static void main(String[] args) throws Throwable {
        int[] data = new int[1000000];
        for (int i = 0; i < data.length; i++)
            data[i] = new Random().nextInt(100);
        System.out.println();
        Sort sort = new Sort();
        for (int i = 1; i < 26; i++) {
            long start = System.currentTimeMillis();
            sort.sort(i, data);
            long end = System.currentTimeMillis();
            System.out.println("" + i + "," + (end - start));
        }
    }

}
