import java.net.URL;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class UA {

    private HttpClient httpclient = new DefaultHttpClient();
    private HttpContext localContext = new BasicHttpContext();
    private CookieStore cookieStore = new BasicCookieStore();

    public UA() {
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
    }

    public void addCookie(String name, String value, String domain, String path) {
        BasicClientCookie cookie = new BasicClientCookie(name, value);
        cookie.setDomain(domain);
        cookie.setPath(path);
        cookieStore.addCookie(cookie);
    }

    private String processResponse(HttpResponse response) throws Exception {
        if (response.getStatusLine().getStatusCode() != 200)
            throw new Exception(response.getStatusLine().getReasonPhrase());
        return EntityUtils.toString(response.getEntity());
    }

    public String get(URL url) throws Exception {
        HttpGet httpget = new HttpGet(url.toString());
        HttpResponse response = httpclient.execute(httpget, localContext);
        return processResponse(response);
    }

    public String post(URL url, String data) throws Exception {
        HttpPost httppost = new HttpPost(url.toString());
        httppost.setEntity(new StringEntity(data));
        HttpResponse response = httpclient.execute(httppost, localContext);
        return processResponse(response);
    }

}
