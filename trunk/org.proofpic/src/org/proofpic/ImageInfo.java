package org.proofpic;

import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
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
            if (getBlobKey(key) == null)
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
        ImageInfo url = new ImageInfo(blobKey, owner);
        PMUtils.pm.makePersistent(url);
        return url;
    }

    public User getOwner() {
    	return owner;
    }
    
    public static String getBlobKey(String key) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query("ImageInfo");
        query.addFilter("key", FilterOperator.EQUAL, key);
        Entity image = datastore.prepare(query).asSingleEntity();
        if (image == null)
        	return null;
        else
        	return image.getProperty("blobKey").toString();
     }
    
    public static String[] loadIds(User owner) {
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      Query query = new Query("ImageInfo");
      query.addFilter("owner", FilterOperator.EQUAL, owner);
      List<Entity> entities = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
      String[] r = new String[entities.size()];
      for (int i = 0; i < r.length; i++)
        r[i] = entities.get(i).getProperty("key").toString();
      return r;
   }
    
    public long getId() {
    	return id;
    }

}
