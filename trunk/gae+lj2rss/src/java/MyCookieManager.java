
import java.net.HttpCookie;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyCookieManager {

    private Map<String, HttpCookie> cookies = new HashMap();

    public MyCookieManager() {
        cookies = new HashMap();
    }

    public void save(URLConnection conn) {
        final List<String> cookieList = conn.getHeaderFields().get("set-cookie");
        if (cookieList == null)
            return;
        for (String cookieString : cookieList) {
            try {
                for (HttpCookie cookie : HttpCookie.parse(cookieString))
                    cookies.put(cookie.getName(), cookie);
            } catch (Exception e) {
            }
        }
    }

    public void load(URLConnection conn) {
        StringBuilder b = new StringBuilder();
        for (HttpCookie cookie : cookies.values()) {
            b.append(cookie.getName());
            b.append("=");
            b.append(cookie.getValue());
            b.append("; ");
        }
        if (!b.toString().isEmpty())
            conn.setRequestProperty("Cookie", b.toString());
    }

    @Override
    public String toString() {
        return cookies.toString();
    }

}
