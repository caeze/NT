package view.util;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Graphics2D utility class.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class Graphics2DUtil {

	public static void activateAntialiasing(Graphics2D g) {
		((Graphics2D) g).setStroke(new BasicStroke(2));
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		rh.add(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		((Graphics2D) g).setRenderingHints(rh);
	}

	public static void deactivateAntialiasing(Graphics2D g) {
		((Graphics2D) g).setStroke(new BasicStroke(1));
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		rh.add(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF));
		((Graphics2D) g).setRenderingHints(rh);
	}
}
