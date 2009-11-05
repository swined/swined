import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class UA {

    private CookieStore cookieStore = new BasicCookieStore();

    public void addCookie(String name, String value) {
        Cookie cookie = new BasicClientCookie(name, value);
        cookieStore.addCookie(cookie);
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

    public String get(URL url) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url.toString());
        HttpContext localContext = new BasicHttpContext();
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        HttpResponse response = httpclient.execute(httpget, localContext);
        if (response.getStatusLine().getStatusCode() != 200)
            throw new Exception(response.getStatusLine().getReasonPhrase());
        return EntityUtils.toString(response.getEntity());
    }

    public String post(URL url, String data) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url.toString());
        httppost.setEntity(new StringEntity(data));
        HttpContext localContext = new BasicHttpContext();
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        HttpResponse response = httpclient.execute(httppost, localContext);
        if (response.getStatusLine().getStatusCode() != 200)
            throw new Exception(response.getStatusLine().getReasonPhrase());
        return EntityUtils.toString(response.getEntity());
    }

    @Override
    public String toString() {
        return cookieStore.toString();
    }

}
