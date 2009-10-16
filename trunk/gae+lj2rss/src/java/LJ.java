import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LJ {

    private UA ua = new UA();

    private String buildSessiongenerateRequest(String username, String hash) {
        RequestBuilder request = new RequestBuilder();
        request.put("mode", "sessiongenerate");
        request.put("expiration", "short");
        request.put("user", username);
        request.put("hpassword", hash);
        return request.toString();
    }

    public void login(String username, String hash) throws Exception {
        final String request = buildSessiongenerateRequest(username, hash);
        LJResponse response = new LJResponse(ua.post(new URL("http://livejournal.com/interface/flat"), request));
        if (null != response.get("errmsg"))
            throw new LJException(response.get("errmsg"));
        String session = response.get("ljsession");
        if (null == session)
            throw new LJException("ljsession not found");
        ua.addCookie("ljsession", session);
    }

    public List<String> links() throws Exception {
        String r = ua.get(new URL("http://www.livejournal.com/mobile/friends.bml"));
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
        String r = ua.get(new URL(url + "?format=light"));
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
