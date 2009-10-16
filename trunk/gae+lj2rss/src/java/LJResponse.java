import java.io.IOException;
import java.util.HashMap;

public class LJResponse extends HashMap<String, String> {

    public LJResponse(String string) throws IOException {
        String k = null;
        for (String l : string.split("\n")) {
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
