package net.swined.parser.core;

public class Terminal implements IMatch {

	private final String ruleId;
	private final String text;
	private final int start;
	private final int end;
	
	public Terminal(String ruleId, String text, int start) {
		this.ruleId = ruleId;
		this.text = text;
		this.start = start;
		this.end = start + text.length();
	}
	
	@Override
	public void accept(IMatchVisitor visitor) {
		if (visitor.enterNode(this))
			visitor.exitNode(this);
	}
	
	@Override
	public int getEnd() {
		return end;
	}
	
	@Override 
	public int getStart() {
		return start;
	}

	@Override
	public String getRuleId() {
		return ruleId;
	}
	
	public String getText() {
		return text;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
}
