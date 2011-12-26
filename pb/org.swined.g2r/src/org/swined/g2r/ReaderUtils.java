package org.swined.g2r;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;

public class ReaderUtils {

	private static final String ENCODING = "UTF-8";
	private static final String CLIENT_NAME = "WebService::Google::Reader/0.21";
	private static final String LOGIN_URL_TEMPLATE = "https://www.google.com/accounts/ClientLogin?service=reader&Email=%s&Passwd=%s&source=%s&ck=%s&client=%s";
	private static final String STARRED_URL_TEMPLATE = "http://www.google.com/reader/atom/user/-/state/com.google/starred?n=%s&r=n&ck=%s&client=%s";
	
	private ReaderUtils() {}
	
	public static String login(String username, String password) throws IOException {
		URL url = url(
				LOGIN_URL_TEMPLATE,
				username,
				password,
				CLIENT_NAME,
				System.currentTimeMillis(),
				CLIENT_NAME
		);
		Properties properties = new Properties();
		properties.load(url.openStream());
		for (String prop : new String[] { "LSID", "Auth", "SID" }) {
			String value = properties.getProperty(prop); 
			if (value == null || value.isEmpty())
				throw new RuntimeException();
		}
		return properties.getProperty("Auth");
	}
	
	private static URL url(String format, Object... args) throws IOException {
		Object[] a = new Object[args.length];
		for (int i = 0; i < a.length; i++)
				a[i] = URLEncoder.encode(args[i].toString(), ENCODING);
		String url = String.format(format, a);
		return new URL(url);
	}
	
	
	public static InputStream starred(String auth, int n) throws IOException {
		URL url = url(
				STARRED_URL_TEMPLATE,
				n,
				System.currentTimeMillis(),
				CLIENT_NAME
		);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestProperty("Authorization", "GoogleLogin auth=" + auth);
		return conn.getInputStream();
	}
	
}
