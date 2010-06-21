package net.swined.parser.core;

public class Chain implements IMatch {

	private final String ruleId;
	private final IMatch[] matches;
	
	public Chain(String ruleId, IMatch[] matches) {
		this.ruleId = ruleId;
		this.matches = matches;
	}
	
	@Override
	public void accept(IMatchVisitor visitor) {
		if (visitor.enterNode(this)) {
			for (IMatch match : matches)
				match.accept(visitor);
			visitor.exitNode(this);
		}
	}

	@Override
	public int getEnd() {
		return matches[matches.length - 1].getEnd();
	}

	@Override
	public String getRuleId() {
		return ruleId;
	}

	@Override
	public int getStart() {
		return matches[0].getStart();
	}

	public IMatch[] getMatches() {
		return matches;
	}
	
	@Override
	public String toString() {
		String r = "";
		for (IMatch match : matches)
			r += match;
		return r;
	}
	
	@Override
	public int getErrCount() {
		int c = 0;
		for (IMatch match : matches)
			c += match.getErrCount();
		return c;
	}

}
