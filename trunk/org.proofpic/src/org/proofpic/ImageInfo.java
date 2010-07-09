package org.proofpic;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.users.User;

@PersistenceCapable
public class ImageInfo {
	
	private final static char[] DIGITS = "1234567890qwertyuiopasdfghjklzxcvbnm".toCharArray();
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private User owner;
    
    @Persistent
    private String key;

    @Persistent
    private String blobKey;

    private ImageInfo(String blobKey, User owner) {
        this.key = generateKey();
        this.blobKey = blobKey;
        this.owner = owner;
    }

    public String getBlobKey() {
        return blobKey;
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

    public static ImageInfo create(String blobKey, User owner) {
        PersistenceManager pm = PMUtils.pm();
        ImageInfo url = new ImageInfo(blobKey, owner);
        pm.makePersistent(url);
        return url;
    }

    public User getOwner() {
    	return owner;
    }
    
    public static ImageInfo load(String key) {
        PersistenceManager pm = PMUtils.pm();
        Query query = pm.newQuery(ImageInfo.class, "key == keyParam");
        query.declareParameters("String keyParam");
        query.setRange(0, 1);
        for (ImageInfo img : (List<ImageInfo>)query.execute(key))
            return img;
        return null;
    }
    
    public long getId() {
    	return id;
    }

}
