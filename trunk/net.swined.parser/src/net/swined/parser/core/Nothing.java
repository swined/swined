package net.swined.parser.core;

public class Nothing implements IMatch {

	private final int offset;

	public Nothing(int offset) {
		this.offset = offset;
	}

	@Override
	public void accept(IMatchVisitor visitor) {
	}

	@Override
	public int getEnd() {
		return offset;
	}

	@Override
	public String getRuleId() {
		return null;
	}

	@Override
	public int getStart() {
		return offset;
	}

}
