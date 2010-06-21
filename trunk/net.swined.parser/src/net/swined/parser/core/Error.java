package net.swined.parser.core;

public class Error implements IMatch {

	private final String ruleId;
	private final int offset;
	private final String text;

	public Error(String ruleId, int offset, String text) {
		this.ruleId = ruleId;
		this.offset = offset;
		this.text = text;
	}

	@Override
	public String getRuleId() {
		return ruleId;
	}

	@Override
	public int getStart() {
		return offset;
	}

	@Override
	public int getEnd() {
		return offset + text.length();
	}

	@Override
	public int getErrCount() {
		return text.length();
	}

	@Override
	public void accept(IMatchVisitor visitor) {
		if (visitor.enterNode(this))
			visitor.exitNode(this);
	}

	@Override
	public String toString() {
		return "<" + ruleId + " expected>";
	}

}
