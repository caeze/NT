package view;

import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import console.Log;
import model.CharacteristicCurve;
import model.RelativePoint;
import preferences.Preferences;
import view.itf.IViewComponent;
import view.util.ButtonUtil;
import view.util.ColorStore;

/**
 * Characteristic curve editor screen.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class CharacteristicCurveEditor implements IViewComponent {
	
	private static final double PADDING = 0.1;

	private static CharacteristicCurveEditor instance;

	private CharacteristicCurveEditorBoard editor = new CharacteristicCurveEditorBoard();
	private CharacteristicCurve curve;
	private RelativePoint northWest = new RelativePoint(PADDING, PADDING);
	private RelativePoint southWest = new RelativePoint(PADDING, 1.0 - PADDING);
	private RelativePoint northEast = new RelativePoint(1.0 - PADDING, PADDING);
	private RelativePoint southEast = new RelativePoint(1.0 - PADDING, 1.0 - PADDING);

	private CharacteristicCurveEditor() {
		// hide constructor, singleton pattern
	}

	/**
	 * Get an instance, singleton pattern.
	 *
	 * @return an instance
	 */
	public static CharacteristicCurveEditor getInstance() {
		if (instance == null) {
			instance = new CharacteristicCurveEditor();
		}
		return instance;
	}

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
	public JComponent initializeViewComponent() {
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
		instance = null;
	}

	private enum Mode {
		NONE, ADD_POINT, MOVE_POINT;
	}

	private class CharacteristicCurveEditorBoard extends JPanel implements ComponentListener, MouseListener, MouseMotionListener, MouseWheelListener {

		private Mode mode = Mode.ADD_POINT;

		private CharacteristicCurveEditorBoard() {
			addComponentListener(this);
			addMouseListener(this);
			addMouseMotionListener(this);
			addMouseWheelListener(this);
			setBackground(ColorStore.BACKGROUND_LIGHT);
			
			if (curve == null) {
				curve = new CharacteristicCurve(UUID.randomUUID(), new ArrayList<>(), "comment");
			}
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			g.setColor(ColorStore.BLACK);
			g.drawLine(northWest.getX(getWidth()), northWest.getY(getHeight()), southWest.getX(getWidth()), southWest.getY(getHeight()));
			g.drawLine(southWest.getX(getWidth()), southWest.getY(getHeight()), southEast.getX(getWidth()), southEast.getY(getHeight()));
			g.drawLine(southEast.getX(getWidth()), southEast.getY(getHeight()), northEast.getX(getWidth()), northEast.getY(getHeight()));
			g.drawLine(northEast.getX(getWidth()), northEast.getY(getHeight()), northWest.getX(getWidth()), northWest.getY(getHeight()));

			g.setColor(ColorStore.CHAIR_COLOR);
			RelativePoint lastPoint = southWest;
			for (RelativePoint p : curve.getPoints()) {
				g.drawLine(lastPoint.getX(getWidth()), lastPoint.getY(getHeight()), p.getX(getWidth()), p.getY(getHeight()));
				lastPoint = p;
			}
		}

		@Override
		public void componentResized(ComponentEvent e) {
		}

		@Override
		public void componentMoved(ComponentEvent e) {
		}

		@Override
		public void componentShown(ComponentEvent e) {
		}

		@Override
		public void componentHidden(ComponentEvent e) {
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (Mode.ADD_POINT.equals(mode)) {
				
			} else if (Mode.MOVE_POINT.equals(mode)) {

			}
			editor.repaint();
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mouseDragged(MouseEvent e) {
		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
		}
	}
}
