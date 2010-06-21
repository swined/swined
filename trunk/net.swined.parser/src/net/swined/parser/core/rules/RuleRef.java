package net.swined.parser.core.rules;

import net.swined.parser.core.IMatch;
import net.swined.parser.core.IRuleResolver;

public class RuleRef implements IRule {

	private final IRuleResolver resolver;
	private final String id;
	
	public RuleRef(IRuleResolver resolver, String id) {
		this.resolver = resolver;
		this.id = id;
	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public IMatch match(String source, int offset, boolean all)
			throws Exception {
		return resolver.getRule(id).match(source, offset, all);
	}

}
