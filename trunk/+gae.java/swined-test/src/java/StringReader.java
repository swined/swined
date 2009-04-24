import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLConnection;

public class StringReader extends BufferedReader {

    public StringReader(URLConnection connection) throws IOException {
        this((InputStream)connection.getContent());
    }

    public StringReader(InputStream stream) {
        this(new InputStreamReader(stream));
    }

    public StringReader(InputStreamReader reader) {
        super(reader);
    }
    
    public String readAll() throws IOException {
        StringBuilder builder = new StringBuilder();
        String line;
        while (null != (line = readLine())) {
            builder.append(line);
            builder.append("\n");
        }
        return builder.toString();
    }

}
