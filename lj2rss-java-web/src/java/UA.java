import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.ClientContext;
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

    private String execute(HttpRequestBase request) throws IOException, HttpException {
        HttpResponse response = httpclient.execute(request, localContext);
        if (response.getStatusLine().getStatusCode() != 200)
            throw new HttpException(response.getStatusLine().getReasonPhrase());
        return EntityUtils.toString(response.getEntity());
    }

    public String get(String url) throws IOException, HttpException {
        return execute(new HttpGet(url));
    }

    public String post(String url, HttpEntity data) throws IOException, HttpException {
        HttpPost req = new HttpPost(url);
        req.setEntity(data);
        return execute(req);
    }

}