package view.util;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import view.img.ImageStore;

/**
 * Menu button utility class.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class ButtonUtil {

	public final static int PERCENTAGE_TO_LIGHT_UP_DEFAULT = 0;
	public final static int PERCENTAGE_TO_LIGHT_UP_HOVER = 120;
	public final static int PERCENTAGE_TO_LIGHT_UP_ON_CLICK = 400;
	public final static float ALPHA_DEFAULT = 0.8f;
	public final static float ALPHA_HOVER = 0.9f;
	public final static float ALPHA_ON_CLICK = 1.0f;

	public static JButton createButton(final Runnable onclick, String iconFileName, String text, int fontSize, Color foregroundColor, String toolTip) {
		ImageIcon icon = ImageStore.createImageIconWithIconAndText(ImageStore.getImageIcon(iconFileName), text, fontSize, foregroundColor);
		return createButton(onclick, icon, icon.getIconWidth(), icon.getIconHeight(), toolTip);
	}

	public static void updateButton(JButton button, String iconFileName, String text, int fontSize, Color foregroundColor, String toolTip) {
		ImageIcon icon = ImageStore.createImageIconWithIconAndText(ImageStore.getImageIcon(iconFileName), text, fontSize, foregroundColor);
		updateButton(button, icon, icon.getIconWidth(), icon.getIconHeight(), toolTip);
	}

	public static JButton createButton(String iconFileName, int width, int height) {
		JButton b = createButton(null, iconFileName, width, height, null);
		b.setEnabled(false);
		b.setFocusable(false);
		b.setSelected(false);
		return b;
	}

	public static JButton createButton(final Runnable onclick, String iconFileName, int width, int height) {
		return createButton(onclick, iconFileName, width, height, null);
	}

	public static JButton createButton(final Runnable onclick, String iconFileName, int width, int height, String toolTip) {
		return createButton(onclick, ImageStore.getImageIcon(iconFileName), width, height, toolTip);
	}

	public static void updateButton(JButton button, ImageIcon icon, int width, int height, String toolTip) {
		button.setIcon(ImageStore.getImageForButton(icon, width, height, 0, ALPHA_DEFAULT));
		button.setDisabledIcon(ImageStore.getImageForButton(icon, width, height, 0, (float) (ALPHA_DEFAULT / 2.0)));
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setContentAreaFilled(false);

		button.addMouseListener(new MouseAdapter() {
			private boolean mouseIsOverButton = false;

			public void mouseEntered(MouseEvent evt) {
				mouseIsOverButton = true;
				button.setIcon(ImageStore.getImageForButton(icon, width, height, PERCENTAGE_TO_LIGHT_UP_HOVER, ALPHA_HOVER));
				if (toolTip != null) {
					button.setToolTipText(toolTip);
				}
			}

			public void mouseExited(MouseEvent evt) {
				mouseIsOverButton = false;
				button.setIcon(ImageStore.getImageForButton(icon, width, height, PERCENTAGE_TO_LIGHT_UP_DEFAULT, ALPHA_DEFAULT));
			}

			public void mousePressed(MouseEvent evt) {
				button.setIcon(ImageStore.getImageForButton(icon, width, height, PERCENTAGE_TO_LIGHT_UP_ON_CLICK, ALPHA_ON_CLICK));
			}

			public void mouseReleased(MouseEvent evt) {
				if (mouseIsOverButton) {
					button.setIcon(ImageStore.getImageForButton(icon, width, height, PERCENTAGE_TO_LIGHT_UP_HOVER, ALPHA_HOVER));
				} else {
					button.setIcon(ImageStore.getImageForButton(icon, width, height, PERCENTAGE_TO_LIGHT_UP_DEFAULT, ALPHA_DEFAULT));
				}
			}

			public void mouseClicked(MouseEvent evt) {
			}
		});
	}

	public static JButton createButton(final Runnable onclick, ImageIcon icon, int width, int height, String toolTip) {
		JButton buttonToReturn = new JButton();
		buttonToReturn.setIcon(ImageStore.getImageForButton(icon, width, height, 0, ALPHA_DEFAULT));
		buttonToReturn.setDisabledIcon(ImageStore.getImageForButton(icon, width, height, 0, (float) (ALPHA_DEFAULT / 2.0)));
		buttonToReturn.setBorder(BorderFactory.createEmptyBorder());
		buttonToReturn.setContentAreaFilled(false);
		if (onclick == null) {
			return buttonToReturn;
		}

		buttonToReturn.addMouseListener(new MouseAdapter() {
			private boolean mouseIsOverButton = false;

			public void mouseEntered(MouseEvent evt) {
				mouseIsOverButton = true;
				buttonToReturn.setIcon(ImageStore.getImageForButton(icon, width, height, PERCENTAGE_TO_LIGHT_UP_HOVER, ALPHA_HOVER));
				if (toolTip != null) {
					buttonToReturn.setToolTipText(toolTip);
				}
			}

			public void mouseExited(MouseEvent evt) {
				mouseIsOverButton = false;
				buttonToReturn.setIcon(ImageStore.getImageForButton(icon, width, height, PERCENTAGE_TO_LIGHT_UP_DEFAULT, ALPHA_DEFAULT));
			}

			public void mousePressed(MouseEvent evt) {
				buttonToReturn.setIcon(ImageStore.getImageForButton(icon, width, height, PERCENTAGE_TO_LIGHT_UP_ON_CLICK, ALPHA_ON_CLICK));
			}

			public void mouseReleased(MouseEvent evt) {
				if (mouseIsOverButton) {
					buttonToReturn.setIcon(ImageStore.getImageForButton(icon, width, height, PERCENTAGE_TO_LIGHT_UP_HOVER, ALPHA_HOVER));
				} else {
					buttonToReturn.setIcon(ImageStore.getImageForButton(icon, width, height, PERCENTAGE_TO_LIGHT_UP_DEFAULT, ALPHA_DEFAULT));
				}
				onclick.run();
			}

			public void mouseClicked(MouseEvent evt) {
			}
		});

		return buttonToReturn;
	}
}
