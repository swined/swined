package org.swined.gae;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Utils {

    public static String httpGet(String url) throws IOException {
        InputStream stream = new URL(url).openStream();
        try {
            return IOUtils.toString(stream);
        } finally {
            stream.close();
        }
    }

}
