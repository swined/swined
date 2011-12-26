package org.swined.g2r;

import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public enum Action {

	AUTH("auth") {
		@Override
		public void invoke(String... args) throws IOException {
			System.out.println(ReaderUtils.login(args[1], args[2]));
		}
	},
	STARRED("starred") {
		@Override
		public void invoke(String... args) throws Throwable {
			String auth = ReaderUtils.login(args[1], args[2]);
			Document atom = AtomUtils.parse(ReaderUtils.starred(auth, 10));
			for (Node node : AtomUtils.getEntries(atom))
				System.out.println(AtomUtils.getTitle(node));
		}
	},
	HELP("help") {
		@Override
		public void invoke(String... args) {
			System.out.println("unknown action " + args[0]);
		}
	};
	
	private final String id;
	
	private Action(String id) {
		this.id = id;
	}
	
	public abstract void invoke(String... args) throws Throwable;
	
	public static void run(String... args) throws Throwable {
		get(args[0]).invoke(args);
	}
	
	public static Action get(String id) {
		for (Action action : values())
			if (action.id.equalsIgnoreCase(id))
				return action;
		return HELP;
	}
	
}
