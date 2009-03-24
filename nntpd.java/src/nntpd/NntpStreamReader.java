package nntpd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class NntpStreamReader extends BufferedReader {

    public NntpStreamReader(InputStream stream) {
        super(new InputStreamReader(stream));
    }

    public String readNntpCommand() throws IOException {
        String line = this.readLine();
        System.out.printf("C: %s\n", line);
        return line;
    }

}
