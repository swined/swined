package net.swined;

import java.net.URLEncoder;
import java.util.HashMap;

public class RequestBulder extends HashMap<String, String> {

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (String k : this.keySet()) {
            builder.append(URLEncoder.encode(k));
            builder.append("=");
            builder.append(URLEncoder.encode(this.get(k)));
            builder.append("&"); // FIXME do not add after the end
        }
        return builder.toString();
    }

}
