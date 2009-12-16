
import cache.ICacheBackend;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PageFetcher implements ICacheBackend<String, String> {

    private String readAll(InputStream stream) throws IOException {
        String l;
        String r = "";
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader buffered = new BufferedReader(reader);
        while (null != (l = buffered.readLine())) {
            r += l + "\n";
        }
        return r;
    }

    public String get(String url) {
        try {
            HttpURLConnection conn = (HttpURLConnection) (new URL(url).openConnection());
            conn.setInstanceFollowRedirects(false);
            //conn.setRequestProperty("user-agent", "http://lj2rss.net.ru/; swined@gmail.com;");
            conn.connect();
            if (conn.getResponseCode() != 200) {
                return null;
            }
            return readAll(conn.getInputStream());
        } catch (Exception e) {
            return null;
        }
    }
}
