package net.swined.parser.core;

import java.util.HashMap;
import java.util.Map;

public class RuleResolver implements IRuleResolver {

	private final Map<String, IRule> rules = new HashMap<String, IRule>();
	
	public void add(IRule rule) {
		rules.put(rule.getId(), rule);
	}
	
	@Override
	public IRule getRule(String id) throws UnknownRuleException {
		if (rules.containsKey(id))
			return rules.get(id);
		else
			throw new UnknownRuleException("rule " + id + " not found");
	}
	
}
