package view;

import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import console.Log;
import control.Control;
import model.RelativePoint;
import nt.NT;
import preferences.Preferences;
import view.img.ImageStore;
import view.itf.IViewComponent;
import view.l10n.L10n;
import view.util.ButtonUtil;
import view.util.ColorStore;

/**
 * Image chooser screen.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class ImageChooser implements IViewComponent {

	private static final double PADDING = 0.1;
	private static final double STUDENT_IMAGE_WIDTH_TO_HEIGHT_RATIO = NT.STUDENT_IMAGE_WIDTH / NT.STUDENT_IMAGE_HEIGHT;

	private ImageChooserBoard editor = new ImageChooserBoard();
	private RelativePoint northWest = new RelativePoint(UUID.randomUUID(), 0.0, 0.0);
	private RelativePoint southEast = new RelativePoint(UUID.randomUUID(), 1.0, 1.0);

	private ImageIcon image;
	private double imageWidthToHeightRatio;

	public ImageChooser(File image) {
		this.image = ImageStore.getImageIcon(image);
		this.imageWidthToHeightRatio = (double) (this.image.getImage().getWidth(null)) / (double) (this.image.getImage().getHeight(null));
	}

	@Override
	public List<JButton> getButtonsLeft() {
		List<JButton> retList = new ArrayList<>();
		JButton backButton = ButtonUtil.createButton(new Runnable() {
			@Override
			public void run() {
				View.getInstance().popViewComponent(IViewComponent.Result.CANCEL);
			}
		}, "back.png", 40, 40, L10n.getString("back"));
		retList.add(backButton);
		JButton doneButton = ButtonUtil.createButton(new Runnable() {
			@Override
			public void run() {
				View.getInstance().popViewComponent(IViewComponent.Result.SAVE);
			}
		}, "done.png", 40, 40, L10n.getString("done"));
		retList.add(doneButton);
		return retList;
	}

	@Override
	public List<JComponent> getComponentsCenter() {
		List<JComponent> retList = new ArrayList<>();
		JButton icon = ButtonUtil.createButton(new Runnable() {
			@Override
			public void run() {
				try {
					if (Desktop.isDesktopSupported()) {
						String s = Preferences.getInstance().projectLocation;
						Desktop.getDesktop().browse(new URI(s.substring(0, s.lastIndexOf("/") + 1)));
					}
				} catch (Exception e) {
					Log.error(ImageChooser.class, e.getMessage());
				}
			}
		}, "logo.png", 48, 48);
		icon.setSelected(true);
		retList.add(icon);
		return retList;
	}

	@Override
	public List<JButton> getButtonsRight() {
		List<JButton> retList = new ArrayList<>();
		retList.add(ButtonUtil.createButton("empty.png", 40, 40));
		JButton exitButton = ButtonUtil.createButton(new Runnable() {
			@Override
			public void run() {
				Control.getInstance().exitProgram();
			}
		}, "clear.png", 40, 40, L10n.getString("exitNT"));
		retList.add(exitButton);
		return retList;
	}

	@Override
	public JComponent initializeViewComponent(boolean firstInitialization) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();

		JPanel retComponent = new JPanel(gridBagLayout);
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.PAGE_START;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		retComponent.add(editor, constraints);

		return retComponent;
	}

	@Override
	public void uninitializeViewComponent() {
	}

	@Override
	public void resultFromLastViewComponent(IViewComponent component, Result result) {
	}

	public ImageIcon getSelectedCroppedImage() {
		ImageIcon selectedCroppedImage = null;
		if (editor.selectionStartingPointX > 0 && editor.selectionStartingPointY > 0 && editor.selectionWidth > 0 && editor.selectionHeight > 0) {
			int northWestX = RelativePoint.relativeToAbsoluteX(northWest.getX(), editor.getWidth(), (int) (PADDING * editor.getWidth()));
			int northWestY = RelativePoint.relativeToAbsoluteY(northWest.getY(), editor.getHeight(), (int) (PADDING * editor.getHeight()));
			int southEastX = RelativePoint.relativeToAbsoluteX(southEast.getX(), editor.getWidth(), (int) (PADDING * editor.getWidth()));
			int southEastY = RelativePoint.relativeToAbsoluteY(southEast.getY(), editor.getHeight(), (int) (PADDING * editor.getHeight()));
			int availableWidth = southEastX - northWestX;
			int availableHeight = southEastY - northWestY;
			int paddingX = northWestX;
			int paddingY = northWestY;
			if ((double) (availableWidth) / (double) (availableHeight) > imageWidthToHeightRatio) {
				availableWidth = (int) (imageWidthToHeightRatio * availableHeight);
				paddingX += (southEastX - northWestX - availableWidth) / 2;
			} else {
				availableHeight = (int) (1 / imageWidthToHeightRatio * availableWidth);
				paddingY += (southEastY - northWestY - availableHeight) / 2;
			}
			double startingRelativeX = (double) (editor.selectionStartingPointX - paddingX) / (double) (availableWidth);
			double startingRelativeY = (double) (editor.selectionStartingPointY - paddingY) / (double) (availableHeight);
			double endingRelativeX = (double) (editor.selectionWidth) / (double) (availableWidth);
			double endingRelativeY = (double) (editor.selectionHeight) / (double) (availableHeight);
			int x = (int) (startingRelativeX * image.getImage().getWidth(null));
			int y = (int) (startingRelativeY * image.getImage().getHeight(null));
			int w = (int) (endingRelativeX * image.getImage().getWidth(null));
			int h = (int) (endingRelativeY * image.getImage().getHeight(null));
			BufferedImage src = ImageStore.fromImageIcon(image);
			selectedCroppedImage = new ImageIcon(src.getSubimage(x, y, w, h));
		} else {
			Log.error(ImageChooser.class, "Could not get selected image!");
		}
		return selectedCroppedImage;
	}

	private enum Mode {
		NONE, SELECT_IMAGE;
	}

	private class ImageChooserBoard extends JPanel implements ComponentListener, MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {

		private Mode mode = Mode.SELECT_IMAGE;

		private RelativePoint selectionStartingPoint = null;
		private RelativePoint selectionEndingPoint = null;

		int selectionStartingPointX = -1;
		int selectionStartingPointY = -1;
		int selectionWidth = -1;
		int selectionHeight = -1;
		int availableWidth = -1;
		int availableHeight = -1;

		private ImageChooserBoard() {
			addComponentListener(this);
			addMouseListener(this);
			addMouseMotionListener(this);
			addMouseWheelListener(this);
			addKeyListener(this);
			setBackground(ColorStore.BACKGROUND_LIGHT);
			requestFocusInWindow();
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			g.setColor(ColorStore.CHAIR_COLOR);
			int northWestX = RelativePoint.relativeToAbsoluteX(northWest.getX(), getWidth(), (int) (PADDING * getWidth()));
			int northWestY = RelativePoint.relativeToAbsoluteY(northWest.getY(), getHeight(), (int) (PADDING * getHeight()));
			int southEastX = RelativePoint.relativeToAbsoluteX(southEast.getX(), getWidth(), (int) (PADDING * getWidth()));
			int southEastY = RelativePoint.relativeToAbsoluteY(southEast.getY(), getHeight(), (int) (PADDING * getHeight()));
			g.drawRect(northWestX, northWestY, southEastX - northWestX, southEastY - northWestY);

			availableWidth = southEastX - northWestX;
			availableHeight = southEastY - northWestY;
			int paddingX = northWestX;
			int paddingY = northWestY;
			if ((double) (availableWidth) / (double) (availableHeight) > imageWidthToHeightRatio) {
				availableWidth = (int) (imageWidthToHeightRatio * availableHeight);
				paddingX += (southEastX - northWestX - availableWidth) / 2;
			} else {
				availableHeight = (int) (1 / imageWidthToHeightRatio * availableWidth);
				paddingY += (southEastY - northWestY - availableHeight) / 2;
			}
			g.drawImage(image.getImage(), paddingX, paddingY, availableWidth, availableHeight, null);

			g.setColor(ColorStore.BACKGROUND_DARK_TRANSPARENT);
			if (selectionStartingPoint != null && selectionEndingPoint != null) {
				int selectionEndingPointX = RelativePoint.relativeToAbsoluteX(selectionEndingPoint.getX(), getWidth(), (int) (PADDING * getWidth()));
				int selectionEndingPointY = RelativePoint.relativeToAbsoluteY(selectionEndingPoint.getY(), getHeight(), (int) (PADDING * getHeight()));

				selectionWidth = selectionEndingPointX - selectionStartingPointX;
				selectionHeight = selectionEndingPointY - selectionStartingPointY;
				g.fillRect(selectionStartingPointX, selectionStartingPointY, selectionWidth, selectionHeight);
			}
		}

		@Override
		public void componentResized(ComponentEvent e) {
			Log.debug(ImageChooser.class, "componentResized, Mode: " + mode);
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			Log.debug(ImageChooser.class, "componentMoved, Mode: " + mode);
		}

		@Override
		public void componentShown(ComponentEvent e) {
			Log.debug(ImageChooser.class, "componentShown, Mode: " + mode);
		}

		@Override
		public void componentHidden(ComponentEvent e) {
			Log.debug(ImageChooser.class, "componentHidden, Mode: " + mode);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			Log.debug(ImageChooser.class, "mouseClicked, Mode: " + mode);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			Log.debug(ImageChooser.class, "mousePressed, Mode: " + mode);

			double x = RelativePoint.absoluteToRelativeX(e.getX(), getWidth(), (int) (PADDING * getWidth()));
			double y = RelativePoint.absoluteToRelativeY(e.getY(), getHeight(), (int) (PADDING * getHeight()));
			if (x >= 0.0 && y >= 0.0 && x <= 1.0 && y <= 1.0) {
				selectionStartingPoint = new RelativePoint(UUID.randomUUID(), x, y);
				selectionEndingPoint = null;
			}

			requestFocusInWindow();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			Log.debug(ImageChooser.class, "mouseReleased, Mode: " + mode);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			Log.debug(ImageChooser.class, "mouseEntered, Mode: " + mode);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			Log.debug(ImageChooser.class, "mouseExited, Mode: " + mode);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			Log.debug(ImageChooser.class, "mouseDragged, Mode: " + mode);
			
			selectionStartingPointX = RelativePoint.relativeToAbsoluteX(selectionStartingPoint.getX(), getWidth(), (int) (PADDING * getWidth()));
			selectionStartingPointY = RelativePoint.relativeToAbsoluteY(selectionStartingPoint.getY(), getHeight(), (int) (PADDING * getHeight()));
			
			int w = e.getX() - selectionStartingPointX;
			int h = e.getY() - selectionStartingPointY;
			if ((double) (w) / (double) (h) < STUDENT_IMAGE_WIDTH_TO_HEIGHT_RATIO) {
				w = (int) (STUDENT_IMAGE_WIDTH_TO_HEIGHT_RATIO * h);
			} else {
				h = (int) (1 / STUDENT_IMAGE_WIDTH_TO_HEIGHT_RATIO * w);
			}

			double x = RelativePoint.absoluteToRelativeX(selectionStartingPointX + w, getWidth(), (int) (PADDING * getWidth()));
			double y = RelativePoint.absoluteToRelativeY(selectionStartingPointY + h, getHeight(), (int) (PADDING * getHeight()));

			if (x >= 0.0 && y >= 0.0 && x <= 1.0 && y <= 1.0) {
				selectionEndingPoint = new RelativePoint(UUID.randomUUID(), x, y);
			} else {
				Log.debug(ImageChooser.class, "Point outside the area! x=" + x + ", y=" + y + ", w=" + w + ", h= " + h);
			}

			repaint();
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			repaint();
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			Log.debug(ImageChooser.class, "mouseWheelMoved, Mode: " + mode);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			Log.debug(ImageChooser.class, "keyPressed, Mode: " + mode);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			Log.debug(ImageChooser.class, "keyReleased, Mode: " + mode);
		}

		@Override
		public void keyTyped(KeyEvent e) {
			Log.debug(ImageChooser.class, "keyTyped, Mode: " + mode);
		}

		@Override
		public boolean isFocusable() {
			return true;
		}
	}
}
