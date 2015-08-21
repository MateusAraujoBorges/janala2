package janala.utils;

import java.util.logging.LogManager;

public class MyLogManager extends LogManager {
	static MyLogManager instance;

	public MyLogManager() {
		instance = this;
	}

	@Override
	public void reset() {
		/* don't reset yet. */ }

	private void reset0() {
		super.reset();
	}

	public static void resetFinally() {
		instance.reset0();
	}
}