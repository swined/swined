package org.prooflink;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

public class PMUtils {

	private static final PersistenceManagerFactory pmf 
		= JDOHelper.getPersistenceManagerFactory("transactions-optional"); 
	
	public static PersistenceManager pm() {
		return pmf.getPersistenceManager();
	}
	
	public static <T> T load(Class<T> c, Long id) {
		PersistenceManager pm = pm();
		return pm.getObjectById(c, id);
	}
	
	public static <T> T save(T o) {
		PersistenceManager pm = pm();
	    try {
	    	return pm.makePersistent(o);
	    } finally {
	    	pm.close();
	    }
	}
	
}
