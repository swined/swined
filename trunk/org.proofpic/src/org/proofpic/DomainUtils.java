package org.proofpic;

import javax.servlet.http.HttpServletRequest;

public class DomainUtils {

	public static String guessSubdomain(HttpServletRequest req) {
		String[] domain = req.getServerName().split("\\.", 2);
		return domain[0];
	}
	
	public static String guessDomain(HttpServletRequest req) {
		String[] domain = req.getServerName().split("\\.", 2);
		return domain[1];
	}
	
}
