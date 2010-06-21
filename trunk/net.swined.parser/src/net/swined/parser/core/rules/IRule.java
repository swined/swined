package net.swined.parser.core.rules;

import net.swined.parser.core.IMatch;

public interface IRule {

	String getId();
	IMatch match(String source, int offset, boolean all) throws Exception;
	
}
