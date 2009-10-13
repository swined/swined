package ln_s;

import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class URL {

    private static final char alphabet[] = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890".toCharArray();

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private String key;

    @Persistent
    private String link;

    private URL(String link) {
        this.key = URL.generateKey();
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public String getKey() {
        return key;
    }

    private static String generateKey() {
        for (int i = 1; true; i++) {
            String key = randStr(i);
            if (load(key) == null)
                return key;
        }
    }

    private static String randStr(int l) {
        StringBuilder r = new StringBuilder();
        for (int i = 0; i < l; i++)
            r.append(alphabet[(int)(Math.random() * alphabet.length)]);
        return r.toString();
    }

    public static URL create(String link) {
            PersistenceManager pm = PMF.get().getPersistenceManager();
            Query query = pm.newQuery(URL.class, "link == linkParam");
            query.declareParameters("String linkParam");
            query.setRange(0, 1);
            for (URL url : (List<URL>)query.execute(link))
                return url;
            URL url = new URL(link);
            pm.makePersistent(url);
            return url;
    }

    public static URL load(String key) {
            PersistenceManager pm = PMF.get().getPersistenceManager();
            Query query = pm.newQuery(URL.class, "key == keyParam");
            query.declareParameters("String keyParam");
            query.setRange(0, 1);
            for (URL url : (List<URL>)query.execute(key))
                return url;
            return null;
    }

}
