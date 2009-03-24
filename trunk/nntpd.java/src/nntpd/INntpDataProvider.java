package nntpd;

import java.util.Collection;

public interface INntpDataProvider {
    Collection<String> groups();
    int estimateCount(String group);
    int getFirstId(String group);
    int getLastId(String group);
}