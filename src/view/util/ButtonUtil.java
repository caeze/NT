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

	public static int PERCENTAGE_TO_LIGHT_UP_DEFAULT = 0;
	public static int PERCENTAGE_TO_LIGHT_UP_HOVER = 500;
	public static int PERCENTAGE_TO_LIGHT_UP_ON_CLICK = 1000;
	public static float ALPHA_DEFAULT = 0.6f;
	public static float ALPHA_HOVER = 0.8f;
	public static float ALPHA_ON_CLICK = 1.0f;

	public static JButton createButton(Runnable onclick, String iconFileName, String text, int fontSize, Color foregroundColor, String toolTip) {
		return createButton(onclick, iconFileName, text, fontSize, foregroundColor, toolTip, 1.0);
	}

	public static JButton createButton(Runnable onclick, String iconFileName, String text, int fontSize, Color foregroundColor, String toolTip, double scalingFactor) {
		ImageIcon icon = ImageStore.createImageIconWithIconAndText(ImageStore.getImageIcon(iconFileName), text, fontSize, foregroundColor);
		return createButton(onclick, icon, (int) (icon.getIconWidth() * scalingFactor), (int) (icon.getIconHeight() * scalingFactor), toolTip, 0.07);
	}

	public static void updateButton(JButton button, String iconFileName, String text, int fontSize, Color foregroundColor, String toolTip) {
		ImageIcon icon = ImageStore.createImageIconWithIconAndText(ImageStore.getImageIcon(iconFileName), text, fontSize, foregroundColor);
		updateButton(button, icon, icon.getIconWidth(), icon.getIconHeight(), toolTip, 0.07);
	}

	public static JButton createButton(String iconFileName, int width, int height) {
		JButton b = createButton(null, iconFileName, width, height, null);
		b.setEnabled(false);
		b.setFocusable(false);
		b.setSelected(false);
		return b;
	}

	public static JButton createButton(Runnable onclick, String iconFileName, int width, int height) {
		return createButton(onclick, iconFileName, width, height, null);
	}

	public static JButton createButton(Runnable onclick, String iconFileName, int width, int height, double factor) {
		return createButton(onclick, iconFileName, width, height, null, factor);
	}

	public static JButton createButton(Runnable onclick, String iconFileName, int width, int height, String toolTip) {
		return createButton(onclick, ImageStore.getImageIcon(iconFileName), width, height, toolTip);
	}

	public static JButton createButton(Runnable onclick, String iconFileName, int width, int height, String toolTip, double factor) {
		return createButton(onclick, ImageStore.getImageIcon(iconFileName), width, height, toolTip, factor);
	}

	public static void updateButton(JButton button, ImageIcon icon, int width, int height, String toolTip) {
		updateButton(button, icon, width, height, toolTip, 1.0);
	}

	public static void updateButton(JButton button, ImageIcon icon, int width, int height, String toolTip, double factor) {
		button.setIcon(ImageStore.getImageForButton(icon, width, height, 0, factor == 1.0 ? ALPHA_DEFAULT : 1));
		button.setDisabledIcon(ImageStore.getImageForButton(icon, width, height, 0, (float) (ALPHA_DEFAULT / 4.0)));
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setContentAreaFilled(false);

		button.addMouseListener(new MouseAdapter() {
			private boolean mouseIsOverButton = false;

			public void mouseEntered(MouseEvent evt) {
				mouseIsOverButton = true;
				button.setIcon(ImageStore.getImageForButton(icon, width, height, (int) (PERCENTAGE_TO_LIGHT_UP_HOVER * factor), factor == 1.0 ? ALPHA_HOVER : 1));
				if (toolTip != null) {
					button.setToolTipText(toolTip);
				}
			}

			public void mouseExited(MouseEvent evt) {
				mouseIsOverButton = false;
				button.setIcon(ImageStore.getImageForButton(icon, width, height, (int) (PERCENTAGE_TO_LIGHT_UP_DEFAULT * factor), factor == 1.0 ? ALPHA_DEFAULT : 1));
			}

			public void mousePressed(MouseEvent evt) {
				button.setIcon(ImageStore.getImageForButton(icon, width, height, (int) (PERCENTAGE_TO_LIGHT_UP_ON_CLICK * factor), factor == 1.0 ? ALPHA_ON_CLICK : 1));
			}

			public void mouseReleased(MouseEvent evt) {
				if (mouseIsOverButton) {
					button.setIcon(ImageStore.getImageForButton(icon, width, height, (int) (PERCENTAGE_TO_LIGHT_UP_HOVER * factor), factor == 1.0 ? ALPHA_HOVER : 1));
				} else {
					button.setIcon(ImageStore.getImageForButton(icon, width, height, (int) (PERCENTAGE_TO_LIGHT_UP_DEFAULT * factor), factor == 1.0 ? ALPHA_DEFAULT : 1));
				}
			}

			public void mouseClicked(MouseEvent evt) {
			}
		});
	}

	public static JButton createButton(Runnable onclick, ImageIcon icon, int width, int height, String toolTip) {
		return createButton(onclick, icon, width, height, toolTip, 1.0);
	}

	public static JButton createButton(Runnable onclick, ImageIcon icon, int width, int height, String toolTip, double factor) {
		JButton buttonToReturn = new JButton();
		buttonToReturn.setIcon(ImageStore.getImageForButton(icon, width, height, 0, factor == 1.0 ? ALPHA_DEFAULT : 1));
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
				buttonToReturn.setIcon(ImageStore.getImageForButton(icon, width, height, (int) (PERCENTAGE_TO_LIGHT_UP_HOVER * factor), factor == 1.0 ? ALPHA_HOVER : 1));
				if (toolTip != null) {
					buttonToReturn.setToolTipText(toolTip);
				}
			}

			public void mouseExited(MouseEvent evt) {
				mouseIsOverButton = false;
				buttonToReturn.setIcon(ImageStore.getImageForButton(icon, width, height, (int) (PERCENTAGE_TO_LIGHT_UP_DEFAULT * factor), factor == 1.0 ? ALPHA_DEFAULT : 1));
			}

			public void mousePressed(MouseEvent evt) {
				buttonToReturn.setIcon(ImageStore.getImageForButton(icon, width, height, (int) (PERCENTAGE_TO_LIGHT_UP_ON_CLICK * factor), factor == 1.0 ? ALPHA_ON_CLICK : 1));
			}

			public void mouseReleased(MouseEvent evt) {
				if (!buttonToReturn.isEnabled()) {
					return;
				}
				if (mouseIsOverButton) {
					buttonToReturn.setIcon(ImageStore.getImageForButton(icon, width, height, (int) (PERCENTAGE_TO_LIGHT_UP_HOVER * factor), factor == 1.0 ? ALPHA_HOVER : 1));
					onclick.run();
					buttonToReturn.setIcon(ImageStore.getImageForButton(icon, width, height, (int) (PERCENTAGE_TO_LIGHT_UP_DEFAULT * factor), factor == 1.0 ? ALPHA_DEFAULT : 1));
				} else {
					buttonToReturn.setIcon(ImageStore.getImageForButton(icon, width, height, (int) (PERCENTAGE_TO_LIGHT_UP_DEFAULT * factor), factor == 1.0 ? ALPHA_DEFAULT : 1));
				}
			}

			public void mouseClicked(MouseEvent evt) {
			}
		});

		return buttonToReturn;
	}
}
