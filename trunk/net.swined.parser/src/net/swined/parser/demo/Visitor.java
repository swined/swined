package net.swined.parser.demo;
import net.swined.parser.core.IMatch;
import net.swined.parser.core.IMatchVisitor;


public class Visitor implements IMatchVisitor {

	private int indent = 0;
	
	@Override
	public boolean enterNode(IMatch match) {
		for (int i = 0; i < indent; i++)
			System.out.print(" ");
		System.out.println(match.getRuleId());
		indent++;
		return true;
	}

	@Override
	public void exitNode(IMatch match) {
		indent--;
	}

}
