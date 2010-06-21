package net.swined.parser.core;

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
	public IMatch match(String source, int offset) {
		try {
			return resolver.getRule(id).match(source, offset);
		} catch (Exception e) {
			return null;
		}
	}

}
