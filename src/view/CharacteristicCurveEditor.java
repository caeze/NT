package view;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import console.Log;
import model.CharacteristicCurve;
import model.GradeType;
import model.RelativePoint;
import preferences.Preferences;
import util.ListUtil;
import view.itf.IViewComponent;
import view.l10n.L10n;
import view.util.ButtonUtil;
import view.util.ColorStore;
import view.util.FloatingPointUtil;
import view.util.Graphics2DUtil;

/**
 * Characteristic curve editor screen.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class CharacteristicCurveEditor implements IViewComponent {

	private static final double PADDING = 0.1;
	private static final int POINT_SIZE = 6;
	private static final double GRID_SIZE = 0.05;
	private static final ListUtil<RelativePoint> LU = new ListUtil<>();
	private static final Font FONT = new Font("Century Schoolbook", 1, 11);

	private CharacteristicCurve curve;
	private GradeType gradeType;
	private RelativePoint highlightedPoint = null;

	private CharacteristicCurveEditorBoard editor = new CharacteristicCurveEditorBoard();
	private RelativePoint northWest = new RelativePoint(UUID.randomUUID(), 0.0, 0.0);
	private RelativePoint southWest = new RelativePoint(UUID.randomUUID(), 0.0, 1.0);
	private RelativePoint northEast = new RelativePoint(UUID.randomUUID(), 1.0, 0.0);
	private List<RelativePoint> gridLinesStartPoints;
	private List<RelativePoint> gridLinesEndPoints;

	@Override
	public List<JButton> getButtonsLeft() {
		List<JButton> retList = new ArrayList<>();
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
					Log.error(CharacteristicCurveEditor.class, e.getMessage());
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
		return retList;
	}

	@Override
	public JComponent initializeViewComponent(boolean f) {
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

	private enum Mode {
		NONE, ADD_POINT, MOVE_POINT;
	}

	private class CharacteristicCurveEditorBoard extends JPanel implements ComponentListener, MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {

		private Mode mode = Mode.MOVE_POINT;

		private CharacteristicCurveEditorBoard() {
			addComponentListener(this);
			addMouseListener(this);
			addMouseMotionListener(this);
			addMouseWheelListener(this);
			addKeyListener(this);
			setBackground(ColorStore.BACKGROUND_LIGHT);
			requestFocusInWindow();

			if (curve == null) {
				ArrayList<RelativePoint> l = new ArrayList<>();
				l.add(new RelativePoint(UUID.randomUUID(), 0, 1));
				l.add(new RelativePoint(UUID.randomUUID(), 1, 0));
				curve = new CharacteristicCurve(UUID.randomUUID(), l, "comment");
			}
			if (gradeType == null) {
				gradeType = GradeType.DefaultGradeTypes.GRADES_1_TO_6;
			}
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			if (gridLinesStartPoints == null || gridLinesEndPoints == null) {
				gridLinesStartPoints = new ArrayList<>();
				gridLinesEndPoints = new ArrayList<>();

				double gridSizeForGradeType = gradeType.getSpacing() / Math.abs(gradeType.getBestGrade() - gradeType.getWorstGrade());
				for (double i = 0; i < 1 + FloatingPointUtil.FLOATING_POINT_DELTA; i += gridSizeForGradeType) {
					gridLinesStartPoints.add(new RelativePoint(UUID.randomUUID(), northWest.getX(), northWest.getY() + i));
					gridLinesEndPoints.add(new RelativePoint(UUID.randomUUID(), northEast.getX(), northEast.getY() + i));
				}
				for (double i = 0; i < 1 + FloatingPointUtil.FLOATING_POINT_DELTA; i += GRID_SIZE) {
					gridLinesStartPoints.add(new RelativePoint(UUID.randomUUID(), northWest.getX() + i, northWest.getY()));
					gridLinesEndPoints.add(new RelativePoint(UUID.randomUUID(), southWest.getX() + i, southWest.getY()));
				}
			}

			g.setColor(ColorStore.BACKGROUND_NORMAL);
			g.setFont(FONT);
			for (int i = 0; i < gridLinesStartPoints.size(); i++) {
				RelativePoint start = gridLinesStartPoints.get(i);
				RelativePoint end = gridLinesEndPoints.get(i);
				int x1 = RelativePoint.relativeToAbsoluteX(start.getX(), getWidth(), (int) (PADDING * getWidth()));
				int y1 = RelativePoint.relativeToAbsoluteY(start.getY(), getHeight(), (int) (PADDING * getHeight()));
				int x2 = RelativePoint.relativeToAbsoluteX(end.getX(), getWidth(), (int) (PADDING * getWidth()));
				int y2 = RelativePoint.relativeToAbsoluteY(end.getY(), getHeight(), (int) (PADDING * getHeight()));
				g.drawLine(x1, y1, x2, y2);

				Graphics2DUtil.activateAntialiasing((Graphics2D) g);
				if (start.getX() == northWest.getX() && end.getX() != northWest.getX()) {
					String text = L10n.getString("grade") + ": " + FloatingPointUtil.f2n(gradeType.getBestGrade() + i * gradeType.getDirection() * gradeType.getSpacing());
					int adv = g.getFontMetrics(FONT).stringWidth(text);
					g.drawString(text, x1 - adv - 5, y1 + 4);
				}
				if (start.getY() == northWest.getY() && end.getY() != northWest.getY()) {
					g.drawString(FloatingPointUtil.f2n(start.getX() * 100, 0) + "%", x1 - 10, y2 + 15);
				}
				Graphics2DUtil.deactivateAntialiasing((Graphics2D) g);
			}
			Graphics2DUtil.activateAntialiasing((Graphics2D) g);
			String text = L10n.getString("amountOfPointsReached");
			int adv = g.getFontMetrics(FONT).stringWidth(text);
			int textX = getWidth() / 2 - adv / 2;
			int textY = RelativePoint.relativeToAbsoluteY(1.0, getHeight(), (int) (PADDING * getHeight()));
			g.drawString(text, textX, textY + 30);
			if (curve.getPoints().size() == 2) {
				text = L10n.getString("clickToAddPoints");
				adv = g.getFontMetrics(FONT).stringWidth(text);
				textX = getWidth() / 2 - adv / 2;
				textY = RelativePoint.relativeToAbsoluteY(0.5, getHeight(), (int) (PADDING * getHeight()));
				g.drawString(text, textX, textY - 100);
			}
			Graphics2DUtil.deactivateAntialiasing((Graphics2D) g);

			g.setColor(ColorStore.CHAIR_COLOR);
			RelativePoint lastPoint = southWest;
			List<RelativePoint> points = LU.makeDeepCopy(curve.getPoints());
			for (RelativePoint p : points) {
				int lastPointX = RelativePoint.relativeToAbsoluteX(lastPoint.getX(), getWidth(), (int) (PADDING * getWidth()));
				int lastPointY = RelativePoint.relativeToAbsoluteY(lastPoint.getY(), getHeight(), (int) (PADDING * getHeight()));
				int currentPointX = RelativePoint.relativeToAbsoluteX(p.getX(), getWidth(), (int) (PADDING * getWidth()));
				int currentPointY = RelativePoint.relativeToAbsoluteY(p.getY(), getHeight(), (int) (PADDING * getHeight()));
				Graphics2DUtil.activateAntialiasing((Graphics2D) g);
				g.drawLine(lastPointX, lastPointY, currentPointX, currentPointY);
				Graphics2DUtil.deactivateAntialiasing((Graphics2D) g);
				g.fillRect(currentPointX - POINT_SIZE / 2, currentPointY - POINT_SIZE / 2, POINT_SIZE, POINT_SIZE);
				lastPoint = p;
			}

			if (highlightedPoint != null) {
				int x = RelativePoint.relativeToAbsoluteX(highlightedPoint.getX(), getWidth(), (int) (PADDING * getWidth()));
				int y = RelativePoint.relativeToAbsoluteY(highlightedPoint.getY(), getHeight(), (int) (PADDING * getHeight()));
				g.drawRect(x - POINT_SIZE, y - POINT_SIZE, (int) (POINT_SIZE * (2 - FloatingPointUtil.FLOATING_POINT_DELTA)), (int) (POINT_SIZE * (2 - FloatingPointUtil.FLOATING_POINT_DELTA)));
			}
		}

		@Override
		public void componentResized(ComponentEvent e) {
			Log.debug(CharacteristicCurveEditor.class, "componentResized, Mode: " + mode);
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			Log.debug(CharacteristicCurveEditor.class, "componentMoved, Mode: " + mode);
		}

		@Override
		public void componentShown(ComponentEvent e) {
			Log.debug(CharacteristicCurveEditor.class, "componentShown, Mode: " + mode);
		}

		@Override
		public void componentHidden(ComponentEvent e) {
			Log.debug(CharacteristicCurveEditor.class, "componentHidden, Mode: " + mode);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			Log.debug(CharacteristicCurveEditor.class, "mouseClicked, Mode: " + mode);

			if (Mode.ADD_POINT.equals(mode)) {
				addPoint(e.getX(), e.getY());
			}
			repaint();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			Log.debug(CharacteristicCurveEditor.class, "mousePressed, Mode: " + mode);

			requestFocusInWindow();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			Log.debug(CharacteristicCurveEditor.class, "mouseReleased, Mode: " + mode);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			Log.debug(CharacteristicCurveEditor.class, "mouseEntered, Mode: " + mode);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			Log.debug(CharacteristicCurveEditor.class, "mouseExited, Mode: " + mode);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			Log.debug(CharacteristicCurveEditor.class, "mouseDragged, Mode: " + mode);

			if (Mode.MOVE_POINT.equals(mode)) {
				removePoint(highlightedPoint);
				highlightedPoint = addPoint(e.getX(), e.getY());
			}
			repaint();
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			highlightedPoint = getPointToHighlight(e.getX(), e.getY());
			mode = Mode.MOVE_POINT;
			if (highlightedPoint == null) {
				mode = Mode.ADD_POINT;
			}
			repaint();
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			Log.debug(CharacteristicCurveEditor.class, "mouseWheelMoved, Mode: " + mode);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			Log.debug(CharacteristicCurveEditor.class, "keyPressed, Mode: " + mode);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			Log.debug(CharacteristicCurveEditor.class, "keyReleased, Mode: " + mode);
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_DELETE) {
				removePoint(highlightedPoint);
				highlightedPoint = null;
				repaint();
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			Log.debug(CharacteristicCurveEditor.class, "keyTyped, Mode: " + mode);
		}

		@Override
		public boolean isFocusable() {
			return true;
		}

		private void removePoint(RelativePoint p) {
			if (p != null) {
				curve.getPoints().remove(p);
			}
		}

		private RelativePoint addPoint(int xClicked, int yClicked) {
			double x = RelativePoint.absoluteToRelativeX(xClicked, getWidth(), (int) (PADDING * getWidth()));
			double y = RelativePoint.absoluteToRelativeY(yClicked, getHeight(), (int) (PADDING * getHeight()));
			if (x > 1.0 || x < 0.0 || y > 1.0 || y < 0.0) {
				return null;
			}

			double gridSizeForGradeType = gradeType.getSpacing() / Math.abs(gradeType.getBestGrade() - gradeType.getWorstGrade());
			for (double i = 0; i < 1 + FloatingPointUtil.FLOATING_POINT_DELTA; i += gridSizeForGradeType) {
				if (Math.abs(i - y) <= gridSizeForGradeType / 2) {
					if (FloatingPointUtil.isWithinMargin(i, 1, FloatingPointUtil.FLOATING_POINT_DELTA)) {
						i = 1;
					}
					y = i;
					break;
				}
			}
			for (double i = 0; i < 1 + FloatingPointUtil.FLOATING_POINT_DELTA; i += GRID_SIZE) {
				if (Math.abs(i - x) <= GRID_SIZE / 2) {
					if (FloatingPointUtil.isWithinMargin(i, 1, FloatingPointUtil.FLOATING_POINT_DELTA)) {
						i = 1;
					}
					x = i;
					break;
				}
			}

			for (RelativePoint p : curve.getPoints()) {
				if (FloatingPointUtil.isWithinMargin(x, p.getX(), FloatingPointUtil.FLOATING_POINT_DELTA)) {
					return null;
				}
			}

			RelativePoint p = new RelativePoint(UUID.randomUUID(), x, y);
			curve.getPoints().add(p);

			Collections.sort(curve.getPoints(), new Comparator<RelativePoint>() {
				@Override
				public int compare(RelativePoint lhs, RelativePoint rhs) {
					if (lhs.getX() > rhs.getX()) {
						return 1;
					} else if (lhs.getX() < rhs.getX()) {
						return -1;
					}
					return 0;
				}
			});

			return p;
		}

		private RelativePoint getPointToHighlight(int xClicked, int yClicked) {
			for (RelativePoint p : curve.getPoints()) {
				if (p.equals(southWest) || p.equals(northEast)) {
					continue;
				}
				int x = RelativePoint.relativeToAbsoluteX(p.getX(), getWidth(), (int) (PADDING * getWidth()));
				int y = RelativePoint.relativeToAbsoluteY(p.getY(), getHeight(), (int) (PADDING * getHeight()));
				if (FloatingPointUtil.isWithinMargin(xClicked, x, POINT_SIZE) && FloatingPointUtil.isWithinMargin(yClicked, y, POINT_SIZE)) {
					return p;
				}
			}
			return null;
		}
	}
}
