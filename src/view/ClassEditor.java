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
import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import console.Log;
import model.RelativePoint;
import model.Student;
import model.Table;
import nt.NT;
import preferences.Preferences;
import util.ListUtil;
import view.img.ImageStore;
import view.itf.IViewComponent;
import view.l10n.L10n;
import view.util.ButtonUtil;
import view.util.ColorStore;
import view.util.FloatingPointUtil;
import view.util.Graphics2DUtil;
import view.util.PopupMenuUtil;
import view.util.ShowToast;

/**
 * Main Menu screen.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class ClassEditor implements IViewComponent {

	private static final double PADDING = 0.1;
	private static final int HIGHLIGHTING_MARGIN = 4;
	private static final ListUtil<Table> LU = new ListUtil<>();
	private static final Font FONT = new Font("Century Schoolbook", 1, 11);
	private static final int STUDENT_IMAGES_SPACING = 10;

	private static ClassEditor instance;

	private ClassEditorBoard editor = new ClassEditorBoard();
	private List<Table> tables = new ArrayList<>();
	private Table highlightedTable = null;
	private Student highlightedStudent = null;
	private Map<Student, Date> studentsToLastTooltipShown = new HashMap<>();

	private RelativePoint northWest = new RelativePoint(0.0, 0.0);
	private RelativePoint southWest = new RelativePoint(0.0, 1.0);
	private RelativePoint northEast = new RelativePoint(1.0, 0.0);
	private RelativePoint southEast = new RelativePoint(1.0, 1.0);

	private ClassEditor() {
		// hide constructor, singleton pattern
	}

	/**
	 * Get an instance, singleton pattern.
	 *
	 * @return an instance
	 */
	public static ClassEditor getInstance() {
		if (instance == null) {
			instance = new ClassEditor();
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
					Log.error(ClassEditor.class, e.getMessage());
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
		NONE, ADD_TABLE, MOVE_TABLE, HIGHLIGHT_STUDENT;
	}

	private class ClassEditorBoard extends JPanel implements ComponentListener, MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {

		private Mode mode = Mode.ADD_TABLE;

		private ClassEditorBoard() {
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

			g.setColor(ColorStore.BACKGROUND_NORMAL);
			g.setFont(FONT);
			int x1 = RelativePoint.relativeToAbsoluteX(northWest.getX(), getWidth(), (int) (PADDING * getWidth()));
			int y1 = RelativePoint.relativeToAbsoluteY(northWest.getY(), getHeight(), (int) (PADDING * getHeight()));
			int x2 = RelativePoint.relativeToAbsoluteX(southWest.getX(), getWidth(), (int) (PADDING * getWidth()));
			int y2 = RelativePoint.relativeToAbsoluteY(southWest.getY(), getHeight(), (int) (PADDING * getHeight()));
			g.drawLine(x1, y1, x2, y2);
			x1 = RelativePoint.relativeToAbsoluteX(southWest.getX(), getWidth(), (int) (PADDING * getWidth()));
			y1 = RelativePoint.relativeToAbsoluteY(southWest.getY(), getHeight(), (int) (PADDING * getHeight()));
			x2 = RelativePoint.relativeToAbsoluteX(southEast.getX(), getWidth(), (int) (PADDING * getWidth()));
			y2 = RelativePoint.relativeToAbsoluteY(southEast.getY(), getHeight(), (int) (PADDING * getHeight()));
			g.drawLine(x1, y1, x2, y2);
			x1 = RelativePoint.relativeToAbsoluteX(southEast.getX(), getWidth(), (int) (PADDING * getWidth()));
			y1 = RelativePoint.relativeToAbsoluteY(southEast.getY(), getHeight(), (int) (PADDING * getHeight()));
			x2 = RelativePoint.relativeToAbsoluteX(northEast.getX(), getWidth(), (int) (PADDING * getWidth()));
			y2 = RelativePoint.relativeToAbsoluteY(northEast.getY(), getHeight(), (int) (PADDING * getHeight()));
			g.drawLine(x1, y1, x2, y2);
			x1 = RelativePoint.relativeToAbsoluteX(northEast.getX(), getWidth(), (int) (PADDING * getWidth()));
			y1 = RelativePoint.relativeToAbsoluteY(northEast.getY(), getHeight(), (int) (PADDING * getHeight()));
			x2 = RelativePoint.relativeToAbsoluteX(northWest.getX(), getWidth(), (int) (PADDING * getWidth()));
			y2 = RelativePoint.relativeToAbsoluteY(northWest.getY(), getHeight(), (int) (PADDING * getHeight()));
			g.drawLine(x1, y1, x2, y2);

			List<Table> tablesCopy = LU.makeDeepCopy(tables);
			for (Table t : tablesCopy) {
				int x = RelativePoint.relativeToAbsoluteX(t.getPosition().getX(), getWidth(), (int) (PADDING * getWidth()));
				int y = RelativePoint.relativeToAbsoluteY(t.getPosition().getY(), getHeight(), (int) (PADDING * getHeight()));
				g.setColor(ColorStore.TABLE_COLOR);
				g.fillRect(x, y, t.getWidth(STUDENT_IMAGES_SPACING), t.getHeight());
				g.setColor(ColorStore.CHAIR_COLOR);
				for (int i = 0; i < t.getStudents().size(); i++) {
					BufferedImage img = ImageStore.createRGBImage(t.getStudents().get(i).getImage(), NT.STUDENT_IMAGE_WIDTH, NT.STUDENT_IMAGE_HEIGHT);
					g.drawImage(img, x + i * (STUDENT_IMAGES_SPACING + NT.STUDENT_IMAGE_WIDTH), y - STUDENT_IMAGES_SPACING - NT.STUDENT_IMAGE_HEIGHT, NT.STUDENT_IMAGE_WIDTH, NT.STUDENT_IMAGE_HEIGHT, this);
				}
				if (highlightedStudent != null && t.getStudents().contains(highlightedStudent)) {
					int i = t.getStudents().indexOf(highlightedStudent);
					y = y - STUDENT_IMAGES_SPACING - NT.STUDENT_IMAGE_HEIGHT;
					int spacing = STUDENT_IMAGES_SPACING * i;
					if (i == 0) {
						spacing = 0;
					}
					x = x + (i * NT.STUDENT_IMAGE_WIDTH + spacing);
					g.drawRect(x - HIGHLIGHTING_MARGIN, y - HIGHLIGHTING_MARGIN, (int) (NT.STUDENT_IMAGE_WIDTH + 2 * HIGHLIGHTING_MARGIN - FloatingPointUtil.FLOATING_POINT_DELTA), (int) (NT.STUDENT_IMAGE_HEIGHT + 2 * HIGHLIGHTING_MARGIN - FloatingPointUtil.FLOATING_POINT_DELTA));
				}
			}

			if (tables.isEmpty()) {
				Graphics2DUtil.activateAntialiasing((Graphics2D) g);
				String text = L10n.getString("clickToAddTables");
				int adv = g.getFontMetrics(FONT).stringWidth(text);
				int textX = getWidth() / 2 - adv / 2;
				int textY = RelativePoint.relativeToAbsoluteY(0.5, getHeight(), (int) (PADDING * getHeight()));
				g.drawString(text, textX, textY - 100);
				Graphics2DUtil.deactivateAntialiasing((Graphics2D) g);
			}

			if (highlightedTable != null) {
				int x = RelativePoint.relativeToAbsoluteX(highlightedTable.getPosition().getX(), getWidth(), (int) (PADDING * getWidth()));
				int y = RelativePoint.relativeToAbsoluteY(highlightedTable.getPosition().getY(), getHeight(), (int) (PADDING * getHeight()));
				g.drawRect(x - HIGHLIGHTING_MARGIN, y - HIGHLIGHTING_MARGIN, (int) (highlightedTable.getWidth(STUDENT_IMAGES_SPACING) + 2 * HIGHLIGHTING_MARGIN - FloatingPointUtil.FLOATING_POINT_DELTA), (int) (highlightedTable.getHeight() + 2 * HIGHLIGHTING_MARGIN - FloatingPointUtil.FLOATING_POINT_DELTA));
			}
		}

		@Override
		public void componentResized(ComponentEvent e) {
			Log.debug(ClassEditor.class, "componentResized, Mode: " + mode);
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			Log.debug(ClassEditor.class, "componentMoved, Mode: " + mode);
		}

		@Override
		public void componentShown(ComponentEvent e) {
			Log.debug(ClassEditor.class, "componentShown, Mode: " + mode);
		}

		@Override
		public void componentHidden(ComponentEvent e) {
			Log.debug(ClassEditor.class, "componentHidden, Mode: " + mode);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			Log.debug(ClassEditor.class, "mouseClicked, Mode: " + mode);

			PopupMenuUtil.getInstance().killAllPopupMenus();

			if (SwingUtilities.isLeftMouseButton(e)) {
				if (Mode.ADD_TABLE.equals(mode)) {
					addTable(e.getX(), e.getY());
				}
			} else if (SwingUtilities.isRightMouseButton(e)) {
				if (Mode.HIGHLIGHT_STUDENT.equals(mode)) {
					Map<String, Runnable> elements = new HashMap<>();
					elements.put(L10n.getString("chooseStudent"), new Runnable() {
						@Override
						public void run() {
							System.out.println("chooseStudent");
							repaint();
						}
					});
					elements.put(L10n.getString("remove"), new Runnable() {
						@Override
						public void run() {
							for (Table t : tables) {
								if (t.getStudents().contains(highlightedStudent)) {
									t.getStudents().remove(highlightedStudent);
								}
							}
							Table tableToRemove = null;
							for (Table t : tables) {
								if (t.getStudents().isEmpty()) {
									tableToRemove = t;
								}
							}
							tables.remove(tableToRemove);
							repaint();
						}
					});
					PopupMenuUtil.getInstance().showPopupMenu(e.getXOnScreen(), e.getYOnScreen(), elements);
				} else if (Mode.MOVE_TABLE.equals(mode)) {
					Map<String, Runnable> elements = new HashMap<>();
					elements.put(L10n.getString("addStudent"), new Runnable() {
						@Override
						public void run() {
							highlightedTable = getTableToHighlight(e.getX(), e.getY());
							highlightedTable.getStudents().add(Student.getDummyStudent());
							repaint();
						}
					});
					elements.put(L10n.getString("remove"), new Runnable() {
						@Override
						public void run() {
							highlightedTable = getTableToHighlight(e.getX(), e.getY());
							tables.remove(highlightedTable);
							repaint();
						}
					});
					PopupMenuUtil.getInstance().showPopupMenu(e.getXOnScreen(), e.getYOnScreen(), elements);
				}
			}
			repaint();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			Log.debug(ClassEditor.class, "mousePressed, Mode: " + mode);

			requestFocusInWindow();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			Log.debug(ClassEditor.class, "mouseReleased, Mode: " + mode);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			Log.debug(ClassEditor.class, "mouseEntered, Mode: " + mode);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			Log.debug(ClassEditor.class, "mouseExited, Mode: " + mode);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			Log.debug(ClassEditor.class, "mouseDragged, Mode: " + mode);

			if (Mode.MOVE_TABLE.equals(mode)) {
				double x = RelativePoint.absoluteToRelativeX(e.getX(), getWidth(), (int) (PADDING * getWidth()));
				double y = RelativePoint.absoluteToRelativeY(e.getY(), getHeight(), (int) (PADDING * getHeight()));
				if (x > 1.0 || x < 0.0 || y > 1.0 || y < 0.0) {
					return;
				}
				highlightedTable.setPosition(new RelativePoint(x, y));
			}
			repaint();
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			highlightedTable = getTableToHighlight(e.getX(), e.getY());
			highlightedStudent = getStudentToHighlight(e.getX(), e.getY());
			if (highlightedTable != null) {
				mode = Mode.MOVE_TABLE;
			} else if (highlightedStudent != null) {
				mode = Mode.HIGHLIGHT_STUDENT;
				showStudentInfo(e.getXOnScreen(), e.getYOnScreen(), highlightedStudent);
			} else {
				mode = Mode.ADD_TABLE;
			}
			repaint();
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			Log.debug(ClassEditor.class, "mouseWheelMoved, Mode: " + mode);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			Log.debug(ClassEditor.class, "keyPressed, Mode: " + mode);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			Log.debug(ClassEditor.class, "keyReleased, Mode: " + mode);
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_DELETE) {
				removeTable(highlightedTable);
				highlightedTable = null;
				repaint();
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			Log.debug(ClassEditor.class, "keyTyped, Mode: " + mode);
		}

		@Override
		public boolean isFocusable() {
			return true;
		}

		private void removeTable(Table t) {
			if (t != null) {
				tables.remove(t);
			}
		}

		private Table addTable(int xClicked, int yClicked) {
			double x = RelativePoint.absoluteToRelativeX(xClicked, getWidth(), (int) (PADDING * getWidth()));
			double y = RelativePoint.absoluteToRelativeY(yClicked, getHeight(), (int) (PADDING * getHeight()));
			if (x > 1.0 || x < 0.0 || y > 1.0 || y < 0.0) {
				return null;
			}

			List<Student> s = new ArrayList<>();
			s.add(Student.getDummyStudent());
			s.add(Student.getDummyStudent());
			Table table = new Table(UUID.randomUUID(), new RelativePoint(x, y), s, "");

			tables.add(table);

			return table;
		}

		private Table getTableToHighlight(int xClicked, int yClicked) {
			for (Table t : tables) {
				int x = RelativePoint.relativeToAbsoluteX(t.getPosition().getX(), getWidth(), (int) (PADDING * getWidth()));
				int y = RelativePoint.relativeToAbsoluteY(t.getPosition().getY(), getHeight(), (int) (PADDING * getHeight()));
				if (FloatingPointUtil.isInBetween(xClicked, x, x + t.getWidth(STUDENT_IMAGES_SPACING)) && FloatingPointUtil.isInBetween(yClicked, y, y + t.getHeight())) {
					return t;
				}
			}
			return null;
		}

		private Student getStudentToHighlight(int xClicked, int yClicked) {
			for (Table t : tables) {
				for (int i = 0; i < t.getStudents().size(); i++) {
					Student s = t.getStudents().get(i);
					int x = RelativePoint.relativeToAbsoluteX(t.getPosition().getX(), getWidth(), (int) (PADDING * getWidth()));
					int y = RelativePoint.relativeToAbsoluteY(t.getPosition().getY(), getHeight(), (int) (PADDING * getHeight()));

					y = y - STUDENT_IMAGES_SPACING - NT.STUDENT_IMAGE_HEIGHT;
					int spacing = STUDENT_IMAGES_SPACING * i;
					if (i == 0) {
						spacing = 0;
					}
					x = x + (i * NT.STUDENT_IMAGE_WIDTH + spacing);

					if (FloatingPointUtil.isInBetween(xClicked, x, x + NT.STUDENT_IMAGE_WIDTH) && FloatingPointUtil.isInBetween(yClicked, y, y + NT.STUDENT_IMAGE_HEIGHT)) {
						return s;
					}
				}
			}
			return null;
		}

		private void showStudentInfo(int x, int y, Student student) {
			Date now = new Date();
			Date studentTooltipLastSeen = studentsToLastTooltipShown.get(student);
			if (studentsToLastTooltipShown.get(student) != null && ButtonUtil.getMsDifferenceBetweenDates(studentTooltipLastSeen, now) < 5000) {
				return;
			}
			studentsToLastTooltipShown.put(student, new Date());
			String text = student.getFirstName() + " " + student.getLastName();
			ShowToast.showToastStatic(text, 3000, x, y + 5, ColorStore.BACKGROUND_DIALOG, 12);
		}
	}
}
