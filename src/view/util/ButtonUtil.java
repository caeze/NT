package view.util;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import javax.swing.BorderFactory;
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
        JButtonWithTimestamp buttonToReturn = new JButtonWithTimestamp(new Date(1000));
        buttonToReturn.setIcon(ImageStore.getImageForButton(iconFileName, width, height, 0, ALPHA_DEFAULT));
        buttonToReturn.setDisabledIcon(ImageStore.getImageForButton(iconFileName, width, height, 0, ALPHA_DEFAULT));
        buttonToReturn.setBorder(BorderFactory.createEmptyBorder());
        buttonToReturn.setContentAreaFilled(false);
        if (onclick == null) {
            return buttonToReturn;
        }

        buttonToReturn.addMouseListener(new MouseAdapter() {
            private boolean mouseIsOverButton = false;

            public void mouseEntered(MouseEvent evt) {
                mouseIsOverButton = true;
                buttonToReturn.setIcon(ImageStore.getImageForButton(iconFileName, width, height, PERCENTAGE_TO_LIGHT_UP_HOVER, ALPHA_HOVER));
                Date now = new Date();
                if (toolTip != null && getMsDifferenceBetweenDates(buttonToReturn.getTimestamp(), now) > 2500) {
                    ShowToast.showToastStatic(toolTip, 1000, evt.getXOnScreen(), evt.getYOnScreen() + 5, ColorStore.BACKGROUND_DIALOG, 12);
                    buttonToReturn.setTimestamp(now);
                }
            }

            public void mouseExited(MouseEvent evt) {
                mouseIsOverButton = false;
                buttonToReturn.setIcon(ImageStore.getImageForButton(iconFileName, width, height, PERCENTAGE_TO_LIGHT_UP_DEFAULT, ALPHA_DEFAULT));
            }

            public void mousePressed(MouseEvent evt) {
                buttonToReturn.setIcon(ImageStore.getImageForButton(iconFileName, width, height, PERCENTAGE_TO_LIGHT_UP_ON_CLICK, ALPHA_ON_CLICK));
            }

            public void mouseReleased(MouseEvent evt) {
                if (mouseIsOverButton) {
                    buttonToReturn.setIcon(ImageStore.getImageForButton(iconFileName, width, height, PERCENTAGE_TO_LIGHT_UP_HOVER, ALPHA_HOVER));
                } else {
                    buttonToReturn.setIcon(ImageStore.getImageForButton(iconFileName, width, height, PERCENTAGE_TO_LIGHT_UP_DEFAULT, ALPHA_DEFAULT));
                }
                onclick.run();
            }

            public void mouseClicked(MouseEvent evt) {
            }
        });

        return buttonToReturn;
    }

    private static class JButtonWithTimestamp extends JButton {

        private Date timestamp;

        private JButtonWithTimestamp(Date timestamp) {
            this.timestamp = timestamp;
        }

        public Date getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Date timestamp) {
            this.timestamp = timestamp;
        }
    }

    private static long getMsDifferenceBetweenDates(Date d1, Date d2) {
        return Math.abs(d2.getTime() - d1.getTime());
    }
}
