import java.util.Vector;
import java.io.InputStream;
import java.net.Authenticator;

public class LJ {
	public static Vector<String> getFriends(Config conf) throws Exception {
		return getFriends(conf, conf.LjUser);
	}

	public static Vector<String> getFriends(Config conf, String journal) throws Exception {
		String url = "http://" + conf.Site + "/misc/fdata.bml?comm=1&user=" + journal;
		InputStream stream = Util.getWebStream(url);
		Vector<String> lines = Util.readLines(stream);
		Vector<String> result = new Vector<String>();
		for (int i = 0; i < lines.size(); i++)
			if (lines.get(i).contains("> "))
				result.add(lines.get(i).replaceAll("^.> ", ""));
		return result;
	}

	public static InputStream getRssStream(Config conf, String journal) throws Exception {
		Authenticator.setDefault(new SimpleAuthenticator(conf.LjUser, conf.LjPass));
		String url = "http://" + conf.Site + "/~" + journal + "/rss?auth=digest";
		return Util.getWebStream(url);
	}
}
