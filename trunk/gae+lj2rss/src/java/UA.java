
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UA {

    private CookieManager cookies = new CookieManager();

    public String GET(URL url) throws Exception {
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        cookies.setCookies(conn);
        conn.setRequestProperty("user-agent", "http://lj2rss.net.ru/; swined@gmail.com;");
        conn.connect();
        cookies.storeCookies(conn);
        if (conn.getResponseCode() != 200)
            throw new Exception("http error " + conn.getResponseCode() + ": " + conn.getResponseMessage() + " while getting " + url.toString());
        String l;
        String r = "";
        InputStreamReader reader = new InputStreamReader(conn.getInputStream());
        BufferedReader buffered = new BufferedReader(reader);
        while (null != (l = buffered.readLine())) {
            r += l + "\n";
        }
        return r;
    }

    @Override
    public String toString() {
        return cookies.toString();
    }

}
