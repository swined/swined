package nsuj02;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class Main {

    private static byte[] longToByte(long[] a) {
        byte[] r = new byte[a.length * 8];
        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < 8; j++)
                r[8 * i + j] = (byte)((a[i] >> (8 * j)) & 0xFF);
        return r;
    }

    private static long[] byteToLong(byte[] a) {
        if (a.length % 8 != 0)
            throw new IllegalArgumentException();
        long[] r = new long[a.length / 8];
        for (int i = 0; i < r.length; i++)
            for (int j = 0; j < 8; j++)
                r[i] += ((0x00 + (long)a[8 * i + j]) & 0xFF) << (8 * j);
        return r;
    }

    private static long[] stringToLong(String[] a) {
        long[] r = new long[a.length];
        for (int i = 0; i < a.length; i++)
            r[i] = Long.parseLong(a[i]);
        return r;
    }

    private static void write(String file, byte[] data) throws IOException {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(data);
        } finally {
            if (outputStream != null)
                outputStream.close();
        }
    }

    private static byte[] read(String file) throws IOException {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            byte[] r = new byte[0];
            byte[] b = new byte[1024];
            int c = -1;
            while (-1 != (c = inputStream.read(b))) {
                byte[] t = new byte[r.length + c];
                System.arraycopy(r, 0, t, 0, r.length);
                System.arraycopy(b, 0, t, r.length, c);
                r = t;
            }
            return r;
        } finally {
            if (inputStream != null)
                inputStream.close();
        }
    }

    public static void main(String[] args) throws Throwable {
        if (args.length < 1)
            throw new IllegalArgumentException();
        String[] nums = Arrays.copyOfRange(args, 1, args.length);
        write(args[0], longToByte(stringToLong(nums)));
        for (long n : byteToLong(read(args[0])))
            System.out.println(n);
    }

}
