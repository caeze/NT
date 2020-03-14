package view.img;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;
import java.io.File;

import javax.swing.ImageIcon;

import console.Log;

/**
 * Image Store for the application.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class ImageStore {

	public static ImageIcon getImageIcon(String name) {
		try {
			if (name != null && !name.isEmpty() && ImageStore.class.getResource(name) != null) {
				return new ImageIcon(ImageStore.class.getResource(name));
			}
		} catch (Exception e) {
			Log.error(ImageStore.class, e.getMessage());
		}

		return createResourceNotFoundImage();
	}

	public static ImageIcon getImageIcon(File f) {
		try {
			if (f != null && f.exists()) {
				return new ImageIcon(f.getAbsolutePath());
			}
		} catch (Exception e) {
			Log.error(ImageStore.class, e.getMessage());
		}

		return createResourceNotFoundImage();
	}

	public static ImageIcon getImageForButton(ImageIcon icon, int width, int height, int percentToLightUp, float newAlpha) {
		try {
			icon = getScaledImage(icon, width, height);
			icon = lightImageUp(icon, percentToLightUp);
			icon = setImageAlphaValues(icon, newAlpha);
		} catch (Exception e) {
			Log.error(ImageStore.class, e.getMessage());
		}
		return icon;
	}

	public static ImageIcon getScaledImage(ImageIcon srcImg, int width, int height) {
		BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = resizedImg.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.drawImage(srcImg.getImage(), 0, 0, width, height, null);
		g2d.dispose();
		return new ImageIcon(resizedImg);
	}

	public static ImageIcon lightImageUp(ImageIcon srcImg, int percentToLightUp) {
		BufferedImage image = new BufferedImage(srcImg.getIconWidth(), srcImg.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		BufferedImage imageTarget = new BufferedImage(srcImg.getIconWidth(), srcImg.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics graphics = image.createGraphics();
		srcImg.paintIcon(null, graphics, 0, 0);
		graphics.dispose();
		Graphics graphics2 = imageTarget.createGraphics();
		srcImg.paintIcon(null, graphics2, 0, 0);
		graphics2.dispose();
		RescaleOp rop = new RescaleOp(1 + (percentToLightUp * 0.01f), 0, null);
		rop.filter(image, imageTarget);
		return new ImageIcon(imageTarget);
	}

	public static ImageIcon setImageAlphaValues(ImageIcon srcImg, float newAlpha) {
		BufferedImage image = new BufferedImage(srcImg.getIconWidth(), srcImg.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = (Graphics2D) image.createGraphics();
		graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, newAlpha));
		srcImg.paintIcon(null, graphics, 0, 0);
		graphics.dispose();
		return new ImageIcon(image);
	}

	public static ImageIcon createImageIconWithIconAndText(ImageIcon srcImg, String text, int fontSize, Color foregroundColor) {
		ImageIcon textIcon = createImageIconFromText(text, fontSize, foregroundColor);
		ImageIcon backgroundIcon = getImageIcon("buttonBackground.png");
		Image image = new BufferedImage(backgroundIcon.getIconWidth(), backgroundIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D) image.getGraphics();
		g2.drawImage(backgroundIcon.getImage(), 0, 0, null);
		g2.drawImage(srcImg.getImage(), 0, 0, null);
		int spacing = (int) (2 + srcImg.getImage().getWidth(null) + (backgroundIcon.getImage().getWidth(null) - srcImg.getImage().getWidth(null) - textIcon.getImage().getWidth(null)) / 2.7);
		g2.drawImage(textIcon.getImage(), spacing, 21, null);
		g2.dispose();
		return new ImageIcon(image);
	}

	public static ImageIcon createImageIconFromText(String text, int fontSize, Color foregroundColor) {
		return createImageIconFromText(text, fontSize, foregroundColor, null);
	}

	public static ImageIcon createImageIconFromText(String text, int fontSize, Color foregroundColor, Color backgroundColor) {
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		Font font = new Font("Arial", Font.PLAIN, fontSize);
		g2d.setFont(font);
		FontMetrics fm = g2d.getFontMetrics();
		int width = (int) fm.getStringBounds(text, g2d.create()).getWidth() + 10;
		int height = fm.getHeight();
		g2d.dispose();

		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		g2d = img.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g2d.setFont(font);
		fm = g2d.getFontMetrics();
		if (backgroundColor != null) {
			g2d.setColor(backgroundColor);
			g2d.fillRect(0, 0, width, height);
		}
		g2d.setColor(foregroundColor);
		g2d.drawString(text, 0, fm.getAscent());
		g2d.dispose();
		return new ImageIcon(img);
	}

	public static BufferedImage createRGBImage(byte[] bytes, int width, int height) {
		DataBufferByte buffer = new DataBufferByte(bytes, bytes.length);
		ColorModel cm = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8, 8 }, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
		return new BufferedImage(cm, Raster.createInterleavedRaster(buffer, width, height, width * 3, 3, new int[] { 0, 1, 2 }, null), false, null);
	}

	public static byte[] getBytesFromImage(ImageIcon imageIcon) {
		BufferedImage img = fromImageIcon(imageIcon);
		WritableRaster raster = img.getRaster();
		DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
		return data.getData();
	}

	public static ImageIcon createResourceNotFoundImage() {
		byte[] imageBytes = { -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1,
				28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, 20, 24, 0,
				0, 0, 53, 0, 0, 0, 0, 0, 0, 31, 16, 53, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, 0, 11, -87, 14, 0, 0, -1, 28, -87, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28,
				0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, 8, 16, -52, 8, 0, 0, -1, 24, -123, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, 25, 28, 0, 0, 0, 95, 20, 6, 0, -1, 28, -52, -1, 28, 0, -1, 28, 0,
				-1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, 31, 28, 0, 0, 6, -123, 0, 0, 0, 14, 0, 0, -1, 28, -87, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, 25, 28, 0, 0, 0, 95, 31, 16, 53, -1, 28,
				0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1,
				28, 0, 8, 16, -52, 8, 0, 0, -1, 24, -123, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, 0, 11, -87, 14, 0, 0, -1, 28, -87, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28,
				0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1,
				28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0,
				-1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0 };
		return new ImageIcon(createRGBImage(imageBytes, 16, 16));
	}

	public static BufferedImage fromImageIcon(ImageIcon icon) {
		BufferedImage bi = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = bi.createGraphics();
		icon.paintIcon(null, g, 0, 0);
		g.dispose();
		return bi;
	}
}
