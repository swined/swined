package org.proofpic;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

public class PMUtils {

	private static final PersistenceManagerFactory pmf 
		= JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	public static final PersistenceManager pm = pmf.getPersistenceManager();
	
	public static <T> T load(Class<T> c, Long id) {
		return pm.getObjectById(c, id);
	}
	
	public static <T> T save(T o) {
	    try {
	    	return pm.makePersistent(o);
	    } finally {
	    	pm.close();
	    }
	}
	
}
