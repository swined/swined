package net.swined;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LJ {

    private HttpURLConnection post(String url, String data) throws MalformedURLException, IOException {
        URL u = new URL(url);
        HttpURLConnection hcon = (HttpURLConnection) u.openConnection();
        hcon.setDoOutput(true);
        hcon.setRequestMethod("POST");
        hcon.connect();
        OutputStream os = hcon.getOutputStream();
        os.write(data.getBytes());
        os.flush();
        os.close();
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
        if (con.getResponseCode() != 200) {
            throw new LJException("http error " + con.getResponseCode() + ": " + con.getResponseMessage());
        }
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

    public String links(String cookies) throws IOException {
        URL u = new URL("http://www.livejournal.com/mobile/friends.bml");
        HttpURLConnection hcon = (HttpURLConnection) u.openConnection();
        hcon.setRequestProperty("cookie", cookies);
        hcon.connect();
        String l;
        String r = "";
        InputStreamReader reader = new InputStreamReader(hcon.getInputStream());
        BufferedReader buffered = new BufferedReader(reader);
        while (null != (l = buffered.readLine())) {
            r += l + "\n";
        }
        return r;
    }

}
