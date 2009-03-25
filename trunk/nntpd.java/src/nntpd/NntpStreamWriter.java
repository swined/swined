package nntpd;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class NntpStreamWriter extends OutputStreamWriter {

    public NntpStreamWriter(OutputStream stream) {
        super(stream);
    }

    public void writeNntpResponse(int code, String text) throws IOException {
        System.out.printf("S: %d %s\n", code, text);
        this.write(code + " " + text + "\n");
        this.flush();
    }

}
