package nsu_j082;

public class SortThread implements Runnable {

    private final Sort sort;
    private final int[] data;

    public SortThread(Sort sort, int[] data) {
        this.sort = sort;
        this.data = data;
    }

    public void run() {
        while (true) {
            boolean found = false;
            for (int i = 1; i < data.length; i++)
                if (data[i - 1] > data[i]) {
                    data[i - 1] ^= data[i];
                    data[i] ^= data[i - 1];
                    data[i - 1] ^= data[i];
                    found = true;
                }
            if (!found)
                break;
        }
        sort.merge(data);
    }

    

}
