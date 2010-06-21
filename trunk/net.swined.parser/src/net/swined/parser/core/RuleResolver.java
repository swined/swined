package net.swined.parser.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleResolver implements IRuleResolver {

	private final Map<String, IRule> rules = new HashMap<String, IRule>();
	
	public void add(IRule rule) {
		rules.put(rule.getId(), rule);
	}
	
	@Override
	public IRule getRule(String id) throws Exception{
		if (rules.containsKey(id))
			return rules.get(id);
		else
			throw new Exception("cannot resove rule '" + id + "'");
	}

	@Override
	public IRule[] getRules(String[] ids) throws Exception {
		List<IRule> r = new ArrayList();
		for (String id : ids)
			r.add(getRule(id));
		return r.toArray(new IRule[0]);
	}

	
	
}
