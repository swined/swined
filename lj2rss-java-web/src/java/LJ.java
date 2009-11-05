import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

public class LJ {

    private UA ua = new UA();

    private HttpEntity buildSessiongenerateRequest(String username, String hash) throws UnsupportedEncodingException {
        List<NameValuePair> data = new ArrayList();
        data.add(new BasicNameValuePair("mode", "sessiongenerate"));
        data.add(new BasicNameValuePair("expiration", "short"));
        data.add(new BasicNameValuePair("user", username));
        data.add(new BasicNameValuePair("hpassword", hash));
        return new UrlEncodedFormEntity(data);
    }

    public void login(String username, String hash) throws Exception {
        final HttpEntity request = buildSessiongenerateRequest(username, hash);
        LJResponse response = new LJResponse(ua.post("http://livejournal.com/interface/flat", request));
        if (null != response.get("errmsg"))
            throw new Exception(response.get("errmsg"));
        String session = response.get("ljsession");
        if (null == session)
            throw new Exception("ljsession not found");
        ua.addCookie("ljsession", session, ".livejournal.com", "/");
    }

    public List<String> links() throws Exception {
        String r = ua.get("http://www.livejournal.com/mobile/friends.bml");
        if (r.contains("You must <a href='login.bml'>log in</a> to read your friends page"))
            throw new Exception("not logged in");
        List<String> links = new ArrayList<String>();
        for (String link : r.split(": <a href='")) {
            if (!link.startsWith("http://"))
                continue;
            String u[] = link.split("\\?");
            if (u.length > 0)
                links.add(u[0]);
        }
        return links;
    }

    private String extractTitle(String html) {
        String split[] = html.split("</?title>", 3);
        if (split.length == 3) {
            return split[1];
        } else {
            return "untitled";
        }
    }

    private String extractBody(String html) {
        String split[] = html.split("</?body ?>", 3);
        if (split.length == 3) {
            return split[1];
        } else {
            return "";
        }
    }

    public String getEntry(String url) throws Exception {
        String r = ua.get(url + "?format=light");
        if (r.split("<blockquote>").length < 2)
            return null;
        String body = extractBody(r);
        String split[] = body.split("<hr />", 3);
        if (split.length == 3) {
            body = split[0] + split[2];
        }
        split = body.split("<br style='clear: both' />", 2);
        if (split.length == 2)
            body = split[0];
        return body;
    }

    @Override
    public String toString() {
        return ua.toString();
    }

}
