package nntpd;

import java.util.ArrayList;
import java.util.Collection;

public class FakeNntpDataProvider implements INntpDataProvider {

    public Collection<String> groups() {
        String[] groups = { "x", "y", "z" };
        Collection<String> result = new ArrayList<String>();
        for (int i = 0; i < groups.length; i++)
            result.add(groups[i]);
        return result;
    }

    public int estimateCount(String group) {
        return 1;
    }

    public int getFirstId(String group) {
        return 1;
    }

    public int getLastId(String group) {
        return 1;
    }

    public String getMsgId(String group, int id) {
        return "<" + id + "@" + group + ".fake>";
    }

}
