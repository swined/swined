
import cache.ICacheBackend;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PageFetcher implements ICacheBackend<String, byte[]> {

    private byte[] readAll(InputStream stream) throws IOException {
        List<Byte> r = new ArrayList();
        while (stream.available() > 0) {
            byte[] b = new byte[1024];
            int c = stream.read(b);
            for (int i = 0; i < c; i++)
                r.add(b[i]);
        }
        byte[] b = new byte[r.size()];
        for (int i = 0; i < r.size(); i++)
            b[i] = r.get(i);
        return b;
    }

    public byte[] get(String url) {
        try {
            HttpURLConnection conn = (HttpURLConnection) (new URL(url).openConnection());
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (conn.getResponseCode() != 200)
                return null;
            return readAll(conn.getInputStream());
        } catch (Exception e) {
            return null;
        }
    }
}
