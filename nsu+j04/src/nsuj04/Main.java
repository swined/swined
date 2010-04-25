package nsuj04;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class Main {


    private static byte[] read(String fileName) throws IOException {
        byte[] file = new byte[0];
        byte[] buffer = new byte[1024];
        int read = 0;
  
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(fileName);
            while ((read = stream.read(buffer)) > 0) {
                byte[] t = new byte[file.length + read];
                System.arraycopy(file, 0, t, 0, file.length);
                System.arraycopy(buffer, 0, t, file.length, read);
                file = t;
            }
        } finally {
            if (stream != null)
                stream.close();
        }
        return file;
    }

    private static void write(String fileName, byte[] data) throws IOException {
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(fileName);
            stream.write(data);
        } finally {
            if (stream != null)
                stream.close();
        }
    }

    public static void main(String[] args) throws Throwable {
        String fn = args[0];
        Charset c1 = Charset.forName(args[1]);
        Charset c2 = Charset.forName(args[2]);
        write(fn, new String(read(fn), c1).getBytes(c2));
    }

}
