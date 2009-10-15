
import java.text.ParseException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

public class CookieManager {

    private Map<String, Map<String, Map<String, String>>> store;

    private static final String SET_COOKIE = "Set-Cookie";
    private static final String COOKIE_VALUE_DELIMITER = ";";
    private static final String PATH = "path";
    private static final String EXPIRES = "expires";
    private static final String DATE_FORMAT = "EEE, dd-MMM-yyyy hh:mm:ss z";
    private static final String SET_COOKIE_SEPARATOR="; ";
    private static final String COOKIE = "Cookie";

    private static final char NAME_VALUE_SEPARATOR = '=';
    private static final char DOT = '.';

    private DateFormat dateFormat;

    public CookieManager() {

	store = new HashMap();
	dateFormat = new SimpleDateFormat(DATE_FORMAT);
    }


    public void storeCookies(URLConnection conn) throws IOException {
	String domain = getDomainFromHost(conn.getURL().getHost());
	Map domainStore;
	if (store.containsKey(domain)) {
	    domainStore = store.get(domain);
	} else {
	    domainStore = new HashMap();
	    store.put(domain, domainStore);
	}
	String headerName=null;
	for (int i=1; (headerName = conn.getHeaderFieldKey(i)) != null; i++) {
	    if (headerName.equalsIgnoreCase(SET_COOKIE)) {
		Map cookie = new HashMap();
		StringTokenizer st = new StringTokenizer(conn.getHeaderField(i), COOKIE_VALUE_DELIMITER);
		if (st.hasMoreTokens()) {
		    String token  = st.nextToken();
		    String name = token.substring(0, token.indexOf(NAME_VALUE_SEPARATOR));
		    String value = token.substring(token.indexOf(NAME_VALUE_SEPARATOR) + 1, token.length());
		    domainStore.put(name, cookie);
		    cookie.put(name, value);
		}
		while (st.hasMoreTokens()) {
		    String token  = st.nextToken();
                    int index = token.indexOf(NAME_VALUE_SEPARATOR);
                    if (index >= 0) {
                        String name = token.substring(0, index).toLowerCase();
                        String value = token.substring(index + 1, token.length());
                        cookie.put(name, value);
                    }
		}
	    }
	}
    }


    public void setCookies(URLConnection conn) throws IOException, ParseException {
	URL url = conn.getURL();
	String domain = getDomainFromHost(url.getHost());
	String path = url.getPath();

	Map<String, Map<String, String>> domainStore = store.get(domain);
	if (domainStore == null) return;
	StringBuffer cookieStringBuffer = new StringBuffer();

        for (String cookieName : domainStore.keySet()) {
	    Map<String, String> cookie = domainStore.get(cookieName);
	    if (comparePaths(cookie.get(PATH), path) && isNotExpired(cookie.get(EXPIRES))) {
		cookieStringBuffer.append(cookieName);
		cookieStringBuffer.append("=");
		cookieStringBuffer.append(cookie.get(cookieName));
		cookieStringBuffer.append(SET_COOKIE_SEPARATOR);
	    }
	}
        conn.setRequestProperty(COOKIE, cookieStringBuffer.toString());
    }

    private String getDomainFromHost(String host) {
	if (host.indexOf(DOT) != host.lastIndexOf(DOT)) {
	    return host.substring(host.indexOf(DOT) + 1);
	} else {
	    return host;
	}
    }

    private boolean isNotExpired(String cookieExpires) throws ParseException {
	if (cookieExpires == null) return true;
	Date now = new Date();
        return (now.compareTo(dateFormat.parse(cookieExpires))) <= 0;
    }

    private boolean comparePaths(String cookiePath, String targetPath) {
	if (cookiePath == null) {
	    return true;
	} else if (cookiePath.equals("/")) {
	    return true;
	} else if (targetPath.regionMatches(0, cookiePath, 0, cookiePath.length())) {
	    return true;
	} else {
	    return false;
	}

    }

    @Override
    public String toString() {
	return store.toString();
    }

}
