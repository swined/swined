package org.prooflink;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Request {
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private Date time;
    
    @Persistent
    private long link;
    
    public Request(long link) {
        this.link = link;
        this.time = new Date();
    }

    public long getLinkId() {
    	return link;
    }
    
    public Link getLink() {
        return PMUtils.load(Link.class, link);
    }

    public Date getTime() {
    	return time;
    }
    
}
