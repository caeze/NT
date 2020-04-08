package nt;

import java.time.format.DateTimeFormatter;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import console.Log;
import control.Control;
import view.util.LoadingAnimation;
import view.util.SplashScreenUtil;

/**
 * Main entry point of the NT application.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class NT {

	public static enum OperatingSystem {
		WINDOWS, LINUX, MACOS
	}

	// basic constants
	public static String VERSION = "0.0.1";
	public static boolean IS_DEBUG = true;
	public static OperatingSystem OS = OperatingSystem.WINDOWS;
	public static int STUDENT_IMAGE_WIDTH = 48;
	public static int STUDENT_IMAGE_HEIGHT = 48;
	public static final String FORMAT_FOR_PERSISTING_DATE = "yyyy-MM-dd";
	public static final String FORMAT_FOR_PERSISTING_DATE_TIME = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String FORMAT_FOR_DISPLAYING_DATE = "dd.MM.yyyy";
	public static final String FORMAT_FOR_DISPLAYING_TIME = "HH:mm:ss";
	public static final String FORMAT_FOR_DISPLAYING_DATE_TIME = "dd.MM.yyyy HH:mm:ss";
	public static final DateTimeFormatter DF_FOR_PERSISTING_DATE = DateTimeFormatter.ofPattern(FORMAT_FOR_PERSISTING_DATE);
	public static final DateTimeFormatter DF_FOR_PERSISTING_DATE_TIME = DateTimeFormatter.ofPattern(FORMAT_FOR_PERSISTING_DATE_TIME);
	public static final DateTimeFormatter DF_FOR_DISPLAYING_DATE = DateTimeFormatter.ofPattern(FORMAT_FOR_DISPLAYING_DATE);
	public static final DateTimeFormatter DF_FOR_DISPLAYING_TIME = DateTimeFormatter.ofPattern(FORMAT_FOR_DISPLAYING_TIME);
	public static final DateTimeFormatter DF_FOR_DISPLAYING_DATE_TIME = DateTimeFormatter.ofPattern(FORMAT_FOR_DISPLAYING_DATE_TIME);

	/**
	 * Main entry point of the application.
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		// determine operating system
		OS = getOperatingSystem();

		// set corresponding look and feel for the whole application
		setLookAndFeel();

		// show a splash screen
		SplashScreenUtil.showSplashScreen("NTLogo.png");
		LoadingAnimation.showLoadingAnim();

		new Thread(new Runnable() {
			@Override
			public void run() {
				if (!IS_DEBUG) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						Log.error(NT.class, "Error on delaying start to show splash screen! " + e.getMessage());
					}
				}

				// set up the program and configuration and start
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						Control.getInstance().init();
					}
				});

				// kill splash screen
				SplashScreenUtil.killSplashScreen();
				LoadingAnimation.killLoadingAnim();
			}
		}).start();
	}

	private static OperatingSystem getOperatingSystem() {
		String os = System.getProperty("os.name", "generic").toLowerCase();
		if ((os.contains("mac")) || (os.contains("darwin"))) {
			return OperatingSystem.MACOS;
		}
		if (os.contains("win")) {
			return OperatingSystem.WINDOWS;
		}
		return OperatingSystem.LINUX;
	}

	private static void setLookAndFeel() {
		switch (OS) {
		case WINDOWS:
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			} catch (Exception e) {
				Log.error(NT.class, e.getMessage());
			}
			break;
		case MACOS:
			try {
				System.setProperty("apple.laf.useScreenMenuBar", "true");
				System.setProperty("com.apple.mrj.application.apple.menu.about.name", "NT");
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				Log.error(NT.class, e.getMessage());
			}
			break;
		case LINUX:
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			} catch (Exception e) {
				Log.error(NT.class, e.getMessage());
			}
			break;
		default:
			break;
		}
	}
}
