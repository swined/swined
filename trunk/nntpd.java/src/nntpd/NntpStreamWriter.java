package nntpd;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class NntpStreamWriter extends OutputStreamWriter {

    public NntpStreamWriter(OutputStream stream) {
        super(stream);
    }

    public void writeNntpResponse(String text) throws IOException {
        System.out.printf("S: %s\n", text);
        this.write(text + "\n");
        this.flush();
    }

}
