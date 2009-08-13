/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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

    public String login(String username, String hash) throws MalformedURLException, IOException {
        String data = "mode=sessiongenerate&expiration=short&user=" + username + "&hpassword=" + hash;
        int len = data.length();
        URL u = new URL("http://livejournal.com/interface/flat");
        HttpURLConnection hcon = (HttpURLConnection)u.openConnection();
        hcon.setDoOutput(true);
        hcon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        hcon.setRequestProperty("Content-Length", ""+len);
        hcon.setRequestMethod("POST");
        hcon.connect();
        String r = null;
        OutputStream os = hcon.getOutputStream();
        os.write(data.getBytes());
        os.flush();
        os.close();
        InputStream is = hcon.getInputStream();
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader buffered = new BufferedReader(reader);
        String line = "";
        while (null != (line = buffered.readLine())) {
            if ("ljsession".equals(line)) {
                if (null != (line = buffered.readLine())) {
                    r = line;
                } else {
                    return null;
                }
            }
            if ("errmsg".equals(line)) {
                return null;
            }
        }
        is.close();
        hcon.disconnect();
        return r;
    }

}
