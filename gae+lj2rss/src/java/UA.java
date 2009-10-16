
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;

public class UA {

    private CookieManager cookies = new CookieManager();

    public void addCookie(String name, String value) {
        cookies.addCookie(new HttpCookie(name, value));
    }

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

    public String post(URL url, String data) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("user-agent", "http://lj2rss.net.ru/; swined@gmail.com;");
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        cookies.load(conn);
        conn.connect();
        OutputStream os = conn.getOutputStream();
        os.write(data.getBytes());
        os.flush();
        os.close();
        cookies.save(conn);
        if (conn.getResponseCode() != 200) {
            throw new Exception("http error " + conn.getResponseCode() + ": " + conn.getResponseMessage());
        }
        return readAll(conn.getInputStream());
    }

    @Override
    public String toString() {
        return cookies.toString();
    }

}
