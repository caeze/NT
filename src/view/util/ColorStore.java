package view.util;

import java.awt.Color;
import javax.swing.plaf.ColorUIResource;

/**
 * Color store utility class.
 * 
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class ColorStore {

    public final static Color GREEN = Color.GREEN;
    public final static Color ORANGE = Color.ORANGE;
    public final static Color YELLOW = Color.YELLOW;
    public final static Color BLUE = Color.BLUE;
    public final static Color RED = Color.RED;
    public final static Color BLACK = Color.BLACK;
    public final static Color GRAY = Color.GRAY;
    public final static Color WHITE = Color.WHITE;

    public static final Color BACKGROUND = new Color(150, 200, 250);
    public static final Color BACKGROUND_DARK = new Color(55, 75, 100);
    public static final Color BACKGROUND_NORMAL = new Color(100, 150, 200);
    public static final Color BACKGROUND_LIGHT = new Color(180, 220, 240);
    public static final Color BACKGROUND_VERY_LIGHT = new Color(230, 240, 250);
    public static final Color BACKGROUND_DIALOG = new Color(65, 100, 100);
    public static final Color BACKGROUND_CONSOLE = new Color(20, 20, 20);
    public static final Color FOREGROUND_CONSOLE = new Color(200, 200, 200);
    public static final Color TABLE_COLOR = new Color(20, 20, 20);
    public static final Color CHAIR_COLOR = new Color(20, 20, 100);

    public static ColorUIResource convertToColorUIResource(Color c) {
        return new ColorUIResource(c.getRed(), c.getGreen(), c.getBlue());
    }
}
