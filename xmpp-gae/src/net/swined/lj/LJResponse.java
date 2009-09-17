package net.swined.lj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class LJResponse extends HashMap<String, String> {

    public LJResponse(InputStream stream) throws IOException {
        String l;
        String k = null;
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader buffered = new BufferedReader(reader);
        while (null != (l = buffered.readLine())) {
            if (null == k) {
                k = l;
            } else {
                this.put(k, l);
                k = null;
            }
        }
        if (null != k) {
            this.put(k, "");
        }
    }
}
