
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.ParseException;

public class UA {

    private MyCookieManager cookies = new MyCookieManager();

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

    private HttpURLConnection getConnection(URL url) throws IOException, ParseException {
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//        conn.setInstanceFollowRedirects(false);
        conn.setRequestProperty("user-agent", "http://lj2rss.net.ru/; swined@gmail.com;");
        return conn;
    }

    public String get(URL url) throws Exception {
        HttpURLConnection conn = getConnection(url);
        cookies.load(conn);
        conn.connect();
        cookies.save(conn);
        if (conn.getResponseCode() != 200)
            throw new Exception("http error " + conn.getResponseCode() + ": " + conn.getResponseMessage() + " while getting " + url.toString());
        return readAll(conn.getInputStream());
    }

    @Override
    public String toString() {
        return cookies.toString();
    }

}