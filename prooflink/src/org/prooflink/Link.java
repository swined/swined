package org.prooflink;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.users.User;

@PersistenceCapable
public class Link {
	
	private final static String DIGITS = "1234567890qwertyuiopasdfghjklzxcvbnm";
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private User owner;

    @Persistent
    private String url;

    public Link() {
    }
    
    public Link(User owner, String url) {
        this.owner = owner;
        this.url = url;
    }

    public Long getId() {
    	return id;
    }
    
    public String getKey() {
    	long k = id;
    	String key = "";
    	while (k > 0) {
    		int digit = (int)(k % DIGITS.length());
    		key = DIGITS.substring(digit, digit + 1) + key;
    		k = k / DIGITS.length();
    	}
    	return key;
    }
    
    public static Link load(String key) {
    	long k = 0;
    	long p = 1;
    	for (int i = key.length(); i > 0; i--) {
    		k += DIGITS.indexOf(key.substring(i - 1, i)) * p;
    		p *= DIGITS.length();
    	}
    	return PMUtils.load(Link.class, k);
    }
    
    public User getOwner() {
    	return owner;
    }
    
    public String getUrl() {
        return url;
    }
    
}