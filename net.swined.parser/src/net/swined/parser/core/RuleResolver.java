package net.swined.parser.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.swined.parser.core.rules.IRule;
import net.swined.parser.core.rules.TerminalRule;

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

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		for (IRule rule : rules.values()) {
			if (rule instanceof TerminalRule)
				b.append("term " + rule.getId() + " := " + rule + "\n");
			else
				b.append("def " + rule.getId() + " := " + rule + "\n");
		}
		return b.toString();
	}
	
}
