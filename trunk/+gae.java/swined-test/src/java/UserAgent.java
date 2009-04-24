import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;

public class UserAgent {

    private Map<String, String> cookies = new Hashtable<String, String>();

    public String get(String url) throws IOException {
        return get(new URL(url));
    }

    public String get(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.connect();
        return new StringReader(connection).readAll();
    }

    public String post(URL url, String data) throws IOException {
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.connect();
        return new StringReader(connection).readAll();
    }


}
