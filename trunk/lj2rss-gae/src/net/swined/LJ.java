package net.swined;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LJ {

    public HttpURLConnection post(String url, String data) throws MalformedURLException, IOException {
        URL u = new URL(url);
        HttpURLConnection hcon = (HttpURLConnection)u.openConnection();
        hcon.setDoOutput(true);
        hcon.setRequestMethod("POST");
        hcon.connect();
        OutputStream os = hcon.getOutputStream();
        os.write(data.getBytes());
        os.flush();
        os.close();
        return hcon;
    }

    public String readAll(InputStream stream) throws IOException {
        StringBuilder builder = new StringBuilder();
        String line;
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader buffered = new BufferedReader(reader);
        while ((line = buffered.readLine()) != null) {
            builder.append(line);
            builder.append("\n");
        }
        return builder.toString();
    }

    public String login(String username, String hash) throws MalformedURLException, IOException {
        RequestBulder request = new RequestBulder();
        request.put("mode", "sessiongenerate");
        request.put("expiration", "short");
        request.put("user", username);
        request.put("hpassword", hash);
        HttpURLConnection hcon = post("http://livejournal.com/interface/flat", request.toString());
        if (hcon.getResponseCode() != 200) {
            return "http err " + hcon.getResponseCode() + ": " + hcon.getResponseMessage();
        }
        StringBuilder b = new StringBuilder();
        b.append(new LJResponse(hcon.getInputStream()).toString());
        b.append("<br>");
        for (String v : hcon.getHeaderFields().get("set-cookie")) {
            b.append(v);
            b.append("<br>");
        }
        return b.toString();
    }

}