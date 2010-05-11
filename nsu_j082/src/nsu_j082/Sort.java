package nsu_j082;

public class Sort {

    private int activeThreads = 0;
    private int[] data = new int[0];

    public synchronized int[] sort(int count, int[] data) throws InterruptedException {
        Thread[] threads = new Thread[count];
        final int len = data.length / count;
        for (int i = 0; i < count; i++) {
            int offset = i * len;
            int length = data.length - offset > len ? len : data.length - offset;
            if (i == count - 1)
                length = data.length - offset;
            int[] chunk = new int[length];
            System.arraycopy(data, offset, chunk, 0, length);
            threads[i] = new Thread(new SortThread(this, chunk));
            threads[i].start();
        }
        activeThreads = count;
        this.data = new int[0];
        while (activeThreads > 0)
            wait();
        return this.data;
    }

    public synchronized void merge(int[] merge) {
        int[] updated = new int[merge.length + data.length];
        int d = 0;
        int m = 0;
        while (m + d < updated.length && m < merge.length && d < data.length) {
            while (m < merge.length && d < data.length && merge[m] >= data[d]) {
                updated[m + d] = data[d];
                d++;
            }
            while (m < merge.length && d < data.length && merge[m] <= data[d]) {
                updated[m + d] = merge[m];
                m++;
            }
        }
        for (; m < merge.length; m++)
            updated[m + d] = merge[m];
        for (; d < data.length; d++)
            updated[m + d] = data[d];
        this.data = updated;
        activeThreads--;
        notifyAll();
    }

}
