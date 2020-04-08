package console;

import java.time.LocalDateTime;

import nt.NT;
import preferences.Preferences;

/**
 * Log utility class.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class Log {

	public static final int LOG_LEVEL_DEBUG = 1;
	public static final int LOG_LEVEL_INFO = 2;
	public static final int LOG_LEVEL_WARNING = 3;
	public static final int LOG_LEVEL_ERROR = 4;

	public static void debug(Class<?> c, String s) {
		if (Preferences.getInstance().logLevel < LOG_LEVEL_INFO) {
			printStdOut(NT.DF_FOR_PERSISTING_DATE.format(LocalDateTime.now()) + " DEBUG: " + c.getName() + ": " + s);
		}
	}

	public static void info(Class<?> c, String s) {
		if (Preferences.getInstance().logLevel < LOG_LEVEL_WARNING) {
			printStdOut(NT.DF_FOR_PERSISTING_DATE.format(LocalDateTime.now()) + " INFO: " + c.getName() + ": " + s);
		}
	}

	public static void warning(Class<?> c, String s) {
		if (Preferences.getInstance().logLevel < LOG_LEVEL_ERROR) {
			printStdOut(NT.DF_FOR_PERSISTING_DATE.format(LocalDateTime.now()) + " WARNING: " + c.getName() + ": " + s);
		}
	}

	public static void error(Class<?> c, String s) {
		printStdErr(NT.DF_FOR_PERSISTING_DATE.format(LocalDateTime.now()) + " ERROR: " + c.getName() + ": " + s);
	}

	private static void printStdOut(String s) {
		System.out.println(s);
	}

	private static void printStdErr(String s) {
		System.err.println(s);
	}
}
