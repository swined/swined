package org.proofpic;

import java.util.List;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.users.User;

public class ImageUtils {
	
	private final static char[] DIGITS = "1234567890qwertyuiopasdfghjklzxcvbnm".toCharArray();
	private final static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
    private ImageUtils() {
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

    public static String create(User owner, BlobKey blobKey) {
    	String key = generateKey();
    	Entity image = new Entity("ImageInfo");
    	image.setProperty("key", key);
    	image.setProperty("blobKey", blobKey.getKeyString());
    	image.setProperty("owner", owner);
    	datastore.put(image);
    	return key;
    }
    
    public static BlobKey getBlobKey(String key) {
        Query query = new Query("ImageInfo");
        query.addFilter("key", FilterOperator.EQUAL, key);
        Entity image = datastore.prepare(query).asSingleEntity();
        if (image == null)
        	return null;
        else
        	return new BlobKey(image.getProperty("blobKey").toString());
     }
    
    public static String[] getKeys(User owner) {
      
      Query query = new Query("ImageInfo");
      query.addFilter("owner", FilterOperator.EQUAL, owner);
      List<Entity> entities = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
      String[] r = new String[entities.size()];
      for (int i = 0; i < r.length; i++)
        r[i] = entities.get(i).getProperty("key").toString();
      return r;
   }
    
}
