
import java.net.HttpCookie;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CookieManager {

    private Map<String, HttpCookie> cookies = new HashMap();

    public CookieManager() {
        cookies = new HashMap();
    }

    public void addCookie(HttpCookie cookie) {
        cookies.put(cookie.getName(), cookie);
    }

    public void save(URLConnection conn) {
        final List<String> cookieList = conn.getHeaderFields().get("set-cookie");
        if (cookieList == null)
            return;
        for (String cookieString : cookieList) {
            try {
                for (HttpCookie cookie : HttpCookie.parse(cookieString))
                    addCookie(cookie);
            } catch (Exception e) {
                System.err.println("failed to parse cookies: " + cookieString);
            }
        }
    }

    public void load(URLConnection conn) {
        String b = this.toString();
        if (!b.isEmpty())
            conn.setRequestProperty("cookie", b);
        System.err.println(conn.getRequestProperty("cookie"));
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (HttpCookie cookie : cookies.values()) {
            b.append(cookie.getName());
            b.append("=");
            b.append(cookie.getValue());
            b.append("; ");
        }
        return b.toString();
    }

}
