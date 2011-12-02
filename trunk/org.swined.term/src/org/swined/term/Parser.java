package org.swined.term;

import java.util.ArrayList;
import java.util.List;

public class Parser {

	public enum MatchLevel {
		YES, NO, MAYBE;
	}
	
	public interface IMatchResult {
		
		MatchLevel getLevel();
		int getLength();
		Object getCapture();
		IRule getNextRule();
		IAction getAction();

		static final IMatchResult NO = new IMatchResult() {
			
			@Override
			public MatchLevel getLevel() {
				return MatchLevel.NO;
			}
			
			@Override
			public IRule getNextRule() {
				return null;
			}
			
			@Override
			public int getLength() {
				return 0;
			}
			
			@Override
			public Object getCapture() {
				return null;
			}
			
			@Override
			public IAction getAction() {
				return null;
			}
		};

		static final IMatchResult MAYBE = new IMatchResult() {
			
			@Override
			public MatchLevel getLevel() {
				return MatchLevel.MAYBE;
			}
			
			@Override
			public IRule getNextRule() {
				return null;
			}
			
			@Override
			public int getLength() {
				return 0;
			}
			
			@Override
			public Object getCapture() {
				return null;
			}
			
			@Override
			public IAction getAction() {
				return null;
			}
		};
		
	}
	
	public interface IRule {
		
		IMatchResult match(char[] buffer, int from, int to);
		
		public static class Choice implements IRule {

			private final IRule[] rules;
			
			public Choice(IRule... rules) {
				this.rules = rules;
			}
			
			@Override
			public IMatchResult match(char[] buffer, int from, int to) {
				boolean seenMaybe = false;
				for (IRule rule : rules) {
					IMatchResult result = rule.match(buffer, from, to);
					switch (result.getLevel()) {
					case YES:
						return result;
					case MAYBE:
						seenMaybe = true;
					}
				}
				return seenMaybe ? IMatchResult.MAYBE : IMatchResult.NO;
			}
			
		}

		public static class Action implements IRule, IMatchResult {
			
			private final IAction action;
			
			public Action(IAction action) {
				this.action = action;
			}

			@Override
			public MatchLevel getLevel() {
				return MatchLevel.YES;
			}

			@Override
			public int getLength() {
				return 0;
			}

			@Override
			public Object getCapture() {
				return null;
			}

			@Override
			public IRule getNextRule() {
				return null;
			}

			@Override
			public IAction getAction() {
				return action;
			}

			@Override
			public IMatchResult match(char[] buffer, int from, int to) {
				return this;
			}
			
		}
		
	}
	
	public interface IAction {
		
		void execute(List<Object> capture);
		
	}
	
	private final char[] buffer = new char[1024];
	private final List<Object> capture = new ArrayList<Object>(10);
	private final IRule root;
	private final IAction death;
	private int from = 0;
	private int to = 0;
	private IRule rule = null;
	
	public Parser(IRule root, IAction death) {
		this.root = root;
		this.rule = root; 
		this.death = death;
	}
	
	public void reset() {
		capture.clear();
		from = 0;
		to = 0;
		rule = root;
	}
	
	public boolean isDone() {
		return from == 0 && to == 0 && rule == root && capture.size() == 0;
	}

	private void die() {
		death.execute(null);
		reset();
	}
	
	public void push(char c) {
		if (to >= buffer.length) {
			die();
			return;
		}
		buffer[to] = c;
		to++;
		suck();
	}
	
	private void suck() {
		IMatchResult result = rule.match(buffer, from, to);
		switch (result.getLevel()) {
		case YES: 
			from += result.getLength();
			if (from > to)
				throw new UnsupportedOperationException();
			if (result.getCapture() != null)
				capture.add(result.getCapture());
			if (result.getNextRule() != null) {
				rule = result.getNextRule();
				break;
			}
			if (result.getAction() != null) {
				result.getAction().execute(capture);
				reset();
				break;
			}
		case NO:
			die();
			break;
		}
	}
	
}
