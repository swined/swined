package net.swined.lj;

import java.net.URLEncoder;
import java.util.HashMap;

public class RequestBulder extends HashMap<String, String> {

    private String urlEncode(String param) {
        if (null == param) {
            return "";
        } else {
            return URLEncoder.encode(param);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (String k : this.keySet()) {
            builder.append(urlEncode(k));
            builder.append("=");
            builder.append(urlEncode(this.get(k)));
            builder.append("&"); // FIXME do not add after the end
        }
        return builder.toString();
    }

}
