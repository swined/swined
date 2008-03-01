import java.util.Vector;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.URL;
import java.net.HttpURLConnection;

public class Util {
	public static Vector<String> readLines(InputStream stream) throws IOException {
		return readLines(new InputStreamReader(stream));
	}

	public static Vector<String> readLines(InputStreamReader reader) throws IOException {
		BufferedReader br = new BufferedReader(reader);
		Vector<String> result = new Vector<String>();
		String line;
		while (null != (line = br.readLine())) 
			result.add(line);
		return result;
	}

	public static InputStream getWebStream(String url) throws Exception {
		URL srv = new URL(url);
		HttpURLConnection conn = (HttpURLConnection)srv.openConnection();
		conn.setInstanceFollowRedirects(true);
		conn.connect();
		return (InputStream)conn.getContent();
	}

	public static boolean strNullOrEmpty(String s) {
		if (s == null)
			return true;
		return s.length() == 0;
	}
}
