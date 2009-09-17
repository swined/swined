package net.swined.lj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LJ {

    private HttpURLConnection post(String url, String data) throws MalformedURLException, IOException, LJException {
        URL u = new URL(url);
        HttpURLConnection hcon = (HttpURLConnection) u.openConnection();
        hcon.setRequestProperty("user-agent", "http://lj2rss.net.ru/; swined@gmail.com;");
        hcon.setDoOutput(true);
        hcon.setRequestMethod("POST");
        hcon.connect();
        OutputStream os = hcon.getOutputStream();
        os.write(data.getBytes());
        os.flush();
        os.close();
        if (hcon.getResponseCode() != 200) {
            throw new LJException("http error " + hcon.getResponseCode() + ": " + hcon.getResponseMessage());
        }
        return hcon;
    }

    private String getCookie(HttpURLConnection con, String name) {
        for (String v : con.getHeaderFields().get("set-cookie")) {
            if (v.startsWith(name + "=")) {
                int i = v.indexOf(";");
                if (i > 0) {
                    return v.substring(0, i);
                }
            }
        }
        return null;
    }

    private String buildSessiongenerateRequest(String username, String hash) {
        RequestBulder request = new RequestBulder();
        request.put("mode", "sessiongenerate");
        request.put("expiration", "short");
        request.put("user", username);
        request.put("hpassword", hash);
        return request.toString();
    }

    public String login(String username, String hash) throws MalformedURLException, IOException, LJException {
        final String request = buildSessiongenerateRequest(username, hash);
        HttpURLConnection con = post("http://livejournal.com/interface/flat", request);
        LJResponse response = new LJResponse(con.getInputStream());
        if (null != response.get("errmsg")) {
            throw new LJException(response.get("errmsg"));
        }
        String session = response.get("ljsession");
        if (null == session) {
            throw new LJException("ljsession not found");
        }
        String uniq = getCookie(con, "ljuniq");
        if (null == session) {
            throw new LJException("ljuniq not found");
        }
        return "ljsession=" + session + "; " + uniq + ";";
    }

    private String get(String cookies, String url) throws IOException, LJException {
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestProperty("cookie", cookies);
        conn.setRequestProperty("user-agent", "http://lj2rss.net.ru/; swined@gmail.com;");
        conn.connect();
        if (conn.getResponseCode() != 200) {
            throw new LJException("http error " + conn.getResponseCode() + ": " + conn.getResponseMessage() + " while getting " + url);
        }
        String l;
        String r = "";
        InputStreamReader reader = new InputStreamReader(conn.getInputStream());
        BufferedReader buffered = new BufferedReader(reader);
        while (null != (l = buffered.readLine())) {
            r += l + "\n";
        }
        return r;
    }

    public List<String> links(String cookies) throws IOException, LJException {
        String r = get(cookies, "http://www.livejournal.com/mobile/friends.bml");
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

    public String getEntry(String cookies, String url) throws IOException, LJException {
        String r = get(cookies, url + "?format=light");
        if (r.split("<blockquote>").length < 2)
            return null;
        String title = extractTitle(r);
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

}
