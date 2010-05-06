package nsuj07;

import java.io.FileInputStream;
import java.io.IOException;

public class MyLoader extends ClassLoader {

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


    public Class defineClass(String fileName) throws IOException {
        byte[] b = read(fileName);
        return defineClass(b, 0, b.length);
    }

}
