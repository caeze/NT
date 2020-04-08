package view.util;

import java.awt.Color;
import java.awt.Dialog;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

import console.Log;
import view.img.ImageStore;

/**
 * Modal splash screen.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class SplashScreenUtil {

	private static JWindow splashScreen;

	/**
	 * Shows a small modal splash screen in the center of the screen.
	 * 
	 * @param image the image to show
	 */
	public static void showSplashScreen(String image) {
		showSplashScreen(ImageStore.getImageIcon(image));
	}

	/**
	 * Shows a small modal splash screen in the center of the screen.
	 * 
	 * @param image the image to show
	 */
	public static void showSplashScreen(ImageIcon image) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					splashScreen = new JWindow();
					JLabel label = new JLabel(image);
					label.setBackground(new Color(0, 0, 0, 0));
					label.setOpaque(true);
					label.setSize(image.getIconWidth(), image.getIconHeight());
					splashScreen.add(label);
					splashScreen.setSize(image.getIconWidth(), image.getIconHeight());
					splashScreen.setLocationRelativeTo(null);
					splashScreen.setBackground(new Color(0, 0, 0, 0));
					splashScreen.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
					splashScreen.setAlwaysOnTop(true);
					splashScreen.setVisible(true);
				} catch (Exception e) {
					Log.error(SplashScreenUtil.class, e.getMessage());
					killSplashScreen();
				}
			}
		});
	}

	/**
	 * Destroys the current splash screen in the center of the screen.
	 */
	public static void killSplashScreen() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					if (splashScreen != null && splashScreen.isDisplayable()) {
						splashScreen.dispose();
						splashScreen = null;
						System.gc();
					}
				} catch (Exception e) {
					Log.error(SplashScreenUtil.class, e.getMessage());
				}
			}
		});
	}
}
