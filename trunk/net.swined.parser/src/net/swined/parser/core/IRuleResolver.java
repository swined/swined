package net.swined.parser.core;

import net.swined.parser.core.rules.IRule;

public interface IRuleResolver {

	IRule getRule(String id) throws Exception;
	IRule[] getRules(String[] ids) throws Exception;
	
}
