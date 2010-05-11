package nsuj08;

import java.io.FileWriter;
import java.io.IOException;

public class SortThread implements Runnable {

    private final int[] data;

    public SortThread(int[] data) {
        this.data = data;
    }

    public int[] getData() {
        return data;
    }

    public void run() {
        int n = 0;
        while (true) {
            System.out.println(Thread.currentThread().getName() + " : " + n++);
            boolean found = false;
            for (int i = 1; i < data.length; i++)
                if (data[i - 1] > data[i]) {
                    data[i - 1] ^= data[i];
                    data[i] ^= data[i - 1];
                    data[i - 1] ^= data[i];
                    found = true;
                    break;
                }
            if (!found)
                break;
        }
        try {
            save(Thread.currentThread().getName() + ".out");
        } catch (Exception e) {
            System.err.println(Thread.currentThread().getName() + " : " + e);
        }
    }

    private void save(String fileName) throws IOException {
        FileWriter writer = null;
        try {
            writer = new FileWriter(fileName);
            for (int i : data)
                writer.write("" + i + "\n");
        } finally {
            if (writer != null)
                writer.close();
        }
    }

}
