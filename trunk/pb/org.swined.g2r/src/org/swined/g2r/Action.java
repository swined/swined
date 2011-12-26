package org.swined.g2r;

import java.io.IOException;
import java.util.Arrays;

public enum Action {

	AUTH("auth") {
		@Override
		public void invoke(String... args) throws IOException {
			System.out.println(ReaderUtils.login(args[0], args[1]));
		}
	},
	STARRED("starred") {
		@Override
		public void invoke(String... args) throws Throwable {
			System.out.println(new String(Utils.read(ReaderUtils.starred(args[0], 10))));
		}
	},
	HELP("help") {
		@Override
		public void invoke(String... args) {
			System.out.println("unknown action");
		}
	};
	
	private final String id;
	
	private Action(String id) {
		this.id = id;
	}
	
	public abstract void invoke(String... args) throws Throwable;
	
	public static void run(String... args) throws Throwable {
		get(args[0]).invoke(Arrays.copyOfRange(args, 1, args.length - 1));
	}
	
	public static Action get(String id) {
		for (Action action : values())
			if (action.id.equalsIgnoreCase(id))
				return action;
		return HELP;
	}
	
}
