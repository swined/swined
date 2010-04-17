package org.prooflink;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.users.User;

@PersistenceCapable
public class Link {
	
	private final static char[] DIGITS = "1234567890qwertyuiopasdfghjklzxcvbnm".toCharArray();
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private User owner;
    
    @Persistent
    private String key;

    @Persistent
    private String link;

    private Link(String link, User owner) {
        this.key = generateKey();
        this.link = link;
        this.owner = owner;
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
            r.append(DIGITS[(int)(Math.random() * DIGITS.length)]);
        return r.toString();
    }

    public static Link create(String link, User owner) {
            PersistenceManager pm = PMUtils.pm();
            Query query = pm.newQuery(Link.class, "link == linkParam");
            query.declareParameters("String linkParam");
            query.setRange(0, 1);
            for (Link url : (List<Link>)query.execute(link))
                return url;
            Link url = new Link(link, owner);
            pm.makePersistent(url);
            return url;
    }

    public User getOwner() {
    	return owner;
    }
    
    public static Link load(String key) {
            PersistenceManager pm = PMUtils.pm();
            Query query = pm.newQuery(Link.class, "key == keyParam");
            query.declareParameters("String keyParam");
            query.setRange(0, 1);
            for (Link url : (List<Link>)query.execute(key))
                return url;
            return null;
    }

}
