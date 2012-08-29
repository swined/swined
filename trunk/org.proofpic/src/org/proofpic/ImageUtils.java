package org.proofpic;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.annotations.Key;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesService.OutputEncoding;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.OutputSettings;
import com.google.appengine.api.images.Transform;
import com.google.appengine.api.users.User;

public class ImageUtils {
	
	private final static Logger log = Logger.getLogger(ImageUtils.class.getName());
	private final static char[] DIGITS = "1234567890qwertyuiopasdfghjklzxcvbnm".toCharArray();
	private final static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	private final static ImagesService imageService = ImagesServiceFactory.getImagesService();
	
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

	public static Size calcImageSize(Entity image) {
		try {
			Image ii = ImagesServiceFactory.makeImageFromBlob(new BlobKey(image.getProperty("blobKey").toString()));
			Transform tr = ImagesServiceFactory.makeImFeelingLucky();
			OutputSettings settings = new OutputSettings(OutputEncoding.JPEG);
			settings.setQuality(1);
			Image ni = imageService.applyTransform(tr, ii, settings);
			return new Size(ni.getWidth(), ni.getHeight());
		} catch (Throwable e) {
			return null;
		}
	}
		
	public static String getServingUrl(Entity image) {
		try {
			return imageService.getServingUrl(new BlobKey(image.getProperty("blobKey").toString()));
		} catch (Throwable e) {
			return null;
		}
	}
	
	public static Size getImageSize(Entity image) {
		if (image.hasProperty("width") && image.hasProperty("height")) {
			System.out.println("got cached entity: " + image.getProperty("key").toString());
			return new Size(Integer.parseInt(image.getProperty("width").toString()), Integer.parseInt(image.getProperty("height").toString()));
		} else {
			Size size = calcImageSize(image);
			if (size != null) {
				image.setProperty("width", size.width);
				image.setProperty("height", size.height);
				com.google.appengine.api.datastore.Key key = datastore.put(image);
				System.out.println("entity saved: " + key.toString());
			}
			return size;
		}
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

    public static List<Entity> getImages(User owner) {
        Query query = new Query("ImageInfo");
        if (owner != null)
      	  query.addFilter("owner", FilterOperator.EQUAL, owner);
        return datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
     }
    
    public static String[] getKeys(User owner) {
      List<Entity> entities = getImages(owner);
      String[] r = new String[entities.size()];
      for (int i = 0; i < r.length; i++)
        r[i] = entities.get(i).getProperty("key").toString();
      return r;
   }
    
}
