package net.swined.parser.demo;
import net.swined.parser.core.ChainRule;
import net.swined.parser.core.IMatch;
import net.swined.parser.core.OrRule;
import net.swined.parser.core.RuleResolver;
import net.swined.parser.core.TerminalRule;

public class Main {

	public static void main(String[] args) throws Exception {
		String[][] terms = new String[][] {
				{"nothing", ""},
				{"eol", "\n"},
				{"eof", "$"},
				{"colon", ";"},
				{"ws", " +"},
				{"rx", "[^\n]*"},
				{"kw_term", "term"},
				{"kw_def", "def"},
				{"id", "[a-zA-Z0-9_]+"},
				{"op_eq", "="},
				{"op_and", "&"},
				{"op_or", "\\|"}
		};
		RuleResolver resolver = new RuleResolver();		
		for (String[] term : terms)
			resolver.add(new TerminalRule(term[0], term[1]));
		resolver.add(new OrRule("eo", new String[] { "eol", "eof", "colon" }));
		resolver.add(new OrRule("eo!f", new String[] { "eol", "colon" }));
		resolver.add(new OrRule("ws?", new String[] {"ws", "nothing"} ));
		resolver.add(new OrRule("and", new String[] { "and_chain", "ID" }));
		resolver.add(new OrRule("or", new String[] { "or_chain", "ID" }));		
		resolver.add(new OrRule("rule", new String[] { "term_rule", "or_rule", "chain_rule" }));
		resolver.add(new OrRule("rule?", new String[] { "rule", "eo!f" }));
		resolver.add(new OrRule("ruleset?", new String[] { "ruleset", "eof" }));
		resolver.add(new ChainRule("ID", new String[] { "ws?", "id", "ws?" }));		
		resolver.add(new ChainRule("or_chain", new String[] { "ID", "op_or", "or" }));
		resolver.add(new ChainRule("and_chain", new String[] { "ID", "op_and", "and" }));
		resolver.add(new ChainRule("term_rule", new String[] { "kw_term", "ws", "ID", "op_eq", "rx", "eo" }));
		resolver.add(new ChainRule("or_rule", new String[] { "kw_def", "ws", "ID", "op_eq", "or", "eo" }));
		resolver.add(new ChainRule("chain_rule", new String[] { "kw_def", "ws", "ID", "op_eq", "and", "eo" }));
		resolver.add(new ChainRule("ruleset", new String[] { "rule?", "ruleset?" }));
		IMatch match = resolver.getRule("ruleset").match(resolver, "def wtf = 42 & asd;\nterm x = aasdf\ndef 123 = asdf | asdf", 0);
		match.accept(new Visitor());
	}
	
}
