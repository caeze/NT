package view;

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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import console.Log;
import control.Control;
import model.LazyAObject;
import model.Model;
import model.RelativePoint;
import model.Student;
import model.StudentOnTableMapping;
import model.Table;
import nt.NT;
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
public class RoomEditor implements IViewComponent {

	public enum Action {
		EDIT_ROOM, EDIT_STUDENTS, EDIT_GRADES
	}

	private static final double PADDING = 0.1;
	private static final int HIGHLIGHTING_MARGIN = 4;
	private static final ListUtil<Table> LU = new ListUtil<>();
	private static final Font FONT = new Font("Century Schoolbook", 1, 11);
	private static final int STUDENT_IMAGES_SPACING = 10;
	private static final String UNKNOWN_STUDENT_IMAGE = "person.png";

	private RoomEditorBoard editor = new RoomEditorBoard();
	private List<Table> tables = new ArrayList<>();
	private List<StudentOnTableMapping> studentsOnTables = new ArrayList<>();
	private Table highlightedTable = null;
	private int highlightedStudentIndex = -1;
	private Map<Student, LocalDateTime> studentsToLastTooltipShown = new HashMap<>();

	private StudentsList sl;
	private Action action;
	private Table tableToSetStudentOn = null;

	private RelativePoint northWest = new RelativePoint(UUID.randomUUID(), 0.0, 0.0);
	private RelativePoint southWest = new RelativePoint(UUID.randomUUID(), 0.0, 1.0);
	private RelativePoint northEast = new RelativePoint(UUID.randomUUID(), 1.0, 0.0);
	private RelativePoint southEast = new RelativePoint(UUID.randomUUID(), 1.0, 1.0);

	public RoomEditor(Action action, List<Table> tables) {
		this(action, tables, new ArrayList<>());
	}

	public RoomEditor(Action action, List<Table> tables, List<StudentOnTableMapping> studentsOnTables) {
		this.action = action;
		this.tables = new ListUtil<Table>().makeDeepCopy(tables);
		this.studentsOnTables = new ListUtil<StudentOnTableMapping>().makeDeepCopy(studentsOnTables);
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
		retList.add(View.getInstance().getLogoButtonForTopCenter());
		return retList;
	}

	@Override
	public List<JButton> getButtonsRight() {
		List<JButton> retList = new ArrayList<>();
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
		if (component.equals(sl)) {
			if (Result.SAVE.equals(result)) {
				Student selectedStudent = sl.getSelectedStudent();
				studentsOnTables.add(new StudentOnTableMapping(UUID.randomUUID(), new LazyAObject<Student>(selectedStudent.getUuid()), new LazyAObject<Table>(tableToSetStudentOn.getUuid()), highlightedStudentIndex));
				tableToSetStudentOn = null;
			} else if (Result.CANCEL.equals(result)) {
			}
		}
	}

	public List<Table> getTables() {
		return tables;
	}

	public List<StudentOnTableMapping> getStudentOnTableMapping() {
		return studentsOnTables;
	}

	/*
	 * private Table getTableOfStudent(Student student) { for (StudentOnTableMapping
	 * sotm : studentsOnTables) { if
	 * (sotm.getStudentUuid().equals(student.getUuid())) { return
	 * Model.getInstance().expandLazyAObject(new
	 * LazyAObject<Table>(sotm.getTableUuid())); } } return null; }
	 */

	private List<Student> getStudentsOnTable(Table t) {
		List<Student> retList = new ArrayList<>();
		for (StudentOnTableMapping sotm : studentsOnTables) {
			if (Model.getInstance().expandLazyAObject(sotm.getTable()).equals(t)) {
				retList.add(Model.getInstance().expandLazyAObject(sotm.getStudent()));
			}
		}
		return retList;
	}

	private enum Mode {
		NONE, ADD_TABLE, MOVE_TABLE, HIGHLIGHT_STUDENT;
	}

	private class RoomEditorBoard extends JPanel implements ComponentListener, MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {

		private Mode mode = Mode.ADD_TABLE;

		private RoomEditorBoard() {
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
				if (action.equals(Action.EDIT_ROOM)) {
					for (int i = 0; i < t.getNumberOfPlaces(); i++) {
						BufferedImage img = ImageStore.fromImageIcon(ImageStore.getScaledImage(ImageStore.getImageIcon(UNKNOWN_STUDENT_IMAGE), NT.STUDENT_IMAGE_WIDTH, NT.STUDENT_IMAGE_HEIGHT));
						g.drawImage(img, x + i * (STUDENT_IMAGES_SPACING + NT.STUDENT_IMAGE_WIDTH), y - STUDENT_IMAGES_SPACING - NT.STUDENT_IMAGE_HEIGHT, NT.STUDENT_IMAGE_WIDTH, NT.STUDENT_IMAGE_HEIGHT, this);
					}
					if (t.equals(highlightedTable) && highlightedStudentIndex != -1) {
						int i = highlightedStudentIndex;
						y = y - STUDENT_IMAGES_SPACING - NT.STUDENT_IMAGE_HEIGHT;
						int spacing = STUDENT_IMAGES_SPACING * i;
						if (i == 0) {
							spacing = 0;
						}
						x = x + (i * NT.STUDENT_IMAGE_WIDTH + spacing);
						g.drawRect(x - HIGHLIGHTING_MARGIN, y - HIGHLIGHTING_MARGIN, (int) (NT.STUDENT_IMAGE_WIDTH + 2 * HIGHLIGHTING_MARGIN - FloatingPointUtil.FLOATING_POINT_DELTA), (int) (NT.STUDENT_IMAGE_HEIGHT + 2 * HIGHLIGHTING_MARGIN - FloatingPointUtil.FLOATING_POINT_DELTA));
					}
				} else if (action.equals(Action.EDIT_STUDENTS)) {
					for (int i = 0; i < t.getNumberOfPlaces(); i++) {
						BufferedImage img = ImageStore.fromImageIcon(ImageStore.getScaledImage(ImageStore.getImageIcon(UNKNOWN_STUDENT_IMAGE), NT.STUDENT_IMAGE_WIDTH, NT.STUDENT_IMAGE_HEIGHT));
						g.drawImage(img, x + i * (STUDENT_IMAGES_SPACING + NT.STUDENT_IMAGE_WIDTH), y - STUDENT_IMAGES_SPACING - NT.STUDENT_IMAGE_HEIGHT, NT.STUDENT_IMAGE_WIDTH, NT.STUDENT_IMAGE_HEIGHT, this);
					}
					for (StudentOnTableMapping sotm : studentsOnTables) {
						if (t.equals(Model.getInstance().expandLazyAObject(sotm.getTable()))) {
							Student s = Model.getInstance().expandLazyAObject(sotm.getStudent());
							BufferedImage img = ImageStore.createRGBImage(s.getImage(), NT.STUDENT_IMAGE_WIDTH, NT.STUDENT_IMAGE_HEIGHT);
							int i = sotm.getPositionOnTable();
							g.drawImage(img, x + i * (STUDENT_IMAGES_SPACING + NT.STUDENT_IMAGE_WIDTH), y - STUDENT_IMAGES_SPACING - NT.STUDENT_IMAGE_HEIGHT, NT.STUDENT_IMAGE_WIDTH, NT.STUDENT_IMAGE_HEIGHT, this);
						}
					}
					if (t.equals(highlightedTable) && highlightedStudentIndex != -1) {
						int i = highlightedStudentIndex;
						y = y - STUDENT_IMAGES_SPACING - NT.STUDENT_IMAGE_HEIGHT;
						int spacing = STUDENT_IMAGES_SPACING * i;
						if (i == 0) {
							spacing = 0;
						}
						x = x + (i * NT.STUDENT_IMAGE_WIDTH + spacing);
						g.drawRect(x - HIGHLIGHTING_MARGIN, y - HIGHLIGHTING_MARGIN, (int) (NT.STUDENT_IMAGE_WIDTH + 2 * HIGHLIGHTING_MARGIN - FloatingPointUtil.FLOATING_POINT_DELTA), (int) (NT.STUDENT_IMAGE_HEIGHT + 2 * HIGHLIGHTING_MARGIN - FloatingPointUtil.FLOATING_POINT_DELTA));
					}
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

			if (highlightedTable != null && highlightedStudentIndex == -1) {
				if (action.equals(Action.EDIT_ROOM)) {
					int x = RelativePoint.relativeToAbsoluteX(highlightedTable.getPosition().getX(), getWidth(), (int) (PADDING * getWidth()));
					int y = RelativePoint.relativeToAbsoluteY(highlightedTable.getPosition().getY(), getHeight(), (int) (PADDING * getHeight()));
					g.drawRect(x - HIGHLIGHTING_MARGIN, y - HIGHLIGHTING_MARGIN, (int) (highlightedTable.getWidth(STUDENT_IMAGES_SPACING) + 2 * HIGHLIGHTING_MARGIN - FloatingPointUtil.FLOATING_POINT_DELTA), (int) (highlightedTable.getHeight() + 2 * HIGHLIGHTING_MARGIN - FloatingPointUtil.FLOATING_POINT_DELTA));
				}
			}

			g.setColor(ColorStore.CHAIR_COLOR);
			int blackboardX1 = RelativePoint.relativeToAbsoluteX(0.4, getWidth(), (int) (PADDING * getWidth()));
			int blackboardY1 = RelativePoint.relativeToAbsoluteY(1.0 - PADDING / 2.0, getHeight(), (int) (PADDING * getHeight()));
			int blackboardX2 = RelativePoint.relativeToAbsoluteX(0.6, getWidth(), (int) (PADDING * getWidth()));
			int blackboardY2 = RelativePoint.relativeToAbsoluteY(1.0 - PADDING / 20.0, getHeight(), (int) (PADDING * getHeight()));
			g.fillRect(blackboardX1, blackboardY1, blackboardX2 - blackboardX1, blackboardY2 - blackboardY1);
			g.setColor(ColorStore.WHITE);
			Graphics2DUtil.activateAntialiasing((Graphics2D) g);
			String text = L10n.getString("blackboard");
			int adv = g.getFontMetrics(FONT).stringWidth(text);
			int textX = getWidth() / 2 - adv / 2;
			int textY = RelativePoint.relativeToAbsoluteY(1.0 - PADDING / 5.5, getHeight(), (int) (PADDING * getHeight()));
			g.drawString(text, textX, textY);
			Graphics2DUtil.deactivateAntialiasing((Graphics2D) g);
		}

		@Override
		public void componentResized(ComponentEvent e) {
			Log.debug(RoomEditor.class, "componentResized, Mode: " + mode);
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			Log.debug(RoomEditor.class, "componentMoved, Mode: " + mode);
		}

		@Override
		public void componentShown(ComponentEvent e) {
			Log.debug(RoomEditor.class, "componentShown, Mode: " + mode);
		}

		@Override
		public void componentHidden(ComponentEvent e) {
			Log.debug(RoomEditor.class, "componentHidden, Mode: " + mode);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			Log.debug(RoomEditor.class, "mouseClicked, Mode: " + mode);

			PopupMenuUtil.getInstance().killAllPopupMenus();

			if (action.equals(Action.EDIT_ROOM)) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					if (Mode.ADD_TABLE.equals(mode)) {
						addTable(e.getX(), e.getY());
					}
				} else if (SwingUtilities.isRightMouseButton(e)) {
					if (Mode.HIGHLIGHT_STUDENT.equals(mode)) {
						Map<String, Runnable> elements = new HashMap<>();
						elements.put(L10n.getString("remove"), new Runnable() {
							@Override
							public void run() {
								int noOfPlaces = highlightedTable.getNumberOfPlaces();
								noOfPlaces--;
								highlightedTable.setNumberOfPlaces(noOfPlaces);
								if (noOfPlaces < 1) {
									removeTable(highlightedTable);
								}
								highlightedStudentIndex = -1;
								highlightedTable = null;
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
								if (highlightedTable == null) {
									getStudentIndexToHighlight(e.getX(), e.getY());
								}
								highlightedTable.setNumberOfPlaces(highlightedTable.getNumberOfPlaces() + 1);
								repaint();
							}
						});
						elements.put(L10n.getString("remove"), new Runnable() {
							@Override
							public void run() {
								highlightedTable = getTableToHighlight(e.getX(), e.getY());
								if (highlightedTable == null) {
									getStudentIndexToHighlight(e.getX(), e.getY());
								}
								removeTable(highlightedTable);
								highlightedTable = null;
								repaint();
							}
						});
						PopupMenuUtil.getInstance().showPopupMenu(e.getXOnScreen(), e.getYOnScreen(), elements);
					}
				}
				repaint();
			} else if (action.equals(Action.EDIT_STUDENTS)) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					if (Mode.HIGHLIGHT_STUDENT.equals(mode)) {
						if (highlightedStudentIndex != -1) {
							sl = new StudentsList(StudentsList.Action.SELECT_STUDENT);
							tableToSetStudentOn = highlightedTable;
							View.getInstance().pushViewComponent(sl);
						}
					}
				} else if (SwingUtilities.isRightMouseButton(e)) {
					if (Mode.HIGHLIGHT_STUDENT.equals(mode)) {
						if (highlightedStudentIndex != -1) {
							Map<String, Runnable> elements = new HashMap<>();
							elements.put(L10n.getString("remove"), new Runnable() {
								@Override
								public void run() {
									StudentOnTableMapping toRemove = null;
									for (StudentOnTableMapping sotm : studentsOnTables) {
										if (sotm.getPositionOnTable() == highlightedStudentIndex) {
											toRemove = sotm;
										}
									}
									studentsOnTables.remove(toRemove);
									repaint();
								}
							});
							PopupMenuUtil.getInstance().showPopupMenu(e.getXOnScreen(), e.getYOnScreen(), elements);
						}
					}
				}
				repaint();
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			Log.debug(RoomEditor.class, "mousePressed, Mode: " + mode);

			requestFocusInWindow();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			Log.debug(RoomEditor.class, "mouseReleased, Mode: " + mode);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			Log.debug(RoomEditor.class, "mouseEntered, Mode: " + mode);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			Log.debug(RoomEditor.class, "mouseExited, Mode: " + mode);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			Log.debug(RoomEditor.class, "mouseDragged, Mode: " + mode);

			if (action.equals(Action.EDIT_ROOM)) {
				if (Mode.MOVE_TABLE.equals(mode)) {
					int width = highlightedTable.getWidth(STUDENT_IMAGES_SPACING);
					int height = highlightedTable.getHeight();
					double x = RelativePoint.absoluteToRelativeX(e.getX() - width / 2, getWidth(), (int) (PADDING * getWidth()));
					double y = RelativePoint.absoluteToRelativeY(e.getY() - height / 2, getHeight(), (int) (PADDING * getHeight()));
					double xC = RelativePoint.absoluteToRelativeX(e.getX(), getWidth(), (int) (PADDING * getWidth()));
					double yC = RelativePoint.absoluteToRelativeY(e.getY(), getHeight(), (int) (PADDING * getHeight()));
					if (xC > 1.0 || xC < 0.0 || yC > 1.0 || yC < 0.0) {
						return;
					}
					highlightedTable.setPosition(new RelativePoint(UUID.randomUUID(), x, y));
				}
				repaint();
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if (action.equals(Action.EDIT_ROOM)) {
				highlightedTable = getTableToHighlight(e.getX(), e.getY());
				highlightedStudentIndex = getStudentIndexToHighlight(e.getX(), e.getY());
				if (highlightedStudentIndex != -1) {
					mode = Mode.HIGHLIGHT_STUDENT;
				} else if (highlightedTable != null) {
					mode = Mode.MOVE_TABLE;
				} else {
					mode = Mode.ADD_TABLE;
				}
				repaint();
			} else if (action.equals(Action.EDIT_STUDENTS)) {
				highlightedTable = getTableToHighlight(e.getX(), e.getY());
				highlightedStudentIndex = getStudentIndexToHighlight(e.getX(), e.getY());
				if (highlightedStudentIndex != -1) {
					mode = Mode.HIGHLIGHT_STUDENT;
					if (!getStudentsOnTable(highlightedTable).isEmpty()) {
						for (StudentOnTableMapping sotm : studentsOnTables) {
							if (Model.getInstance().expandLazyAObject(sotm.getTable()).equals(highlightedTable) && sotm.getPositionOnTable() == highlightedStudentIndex) {
								showStudentInfo(e.getXOnScreen(), e.getYOnScreen(), Model.getInstance().expandLazyAObject(sotm.getStudent()));
							}
						}
					}
				}
				repaint();
			}
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			Log.debug(RoomEditor.class, "mouseWheelMoved, Mode: " + mode);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			Log.debug(RoomEditor.class, "keyPressed, Mode: " + mode);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (action.equals(Action.EDIT_ROOM)) {
				Log.debug(RoomEditor.class, "keyReleased, Mode: " + mode);
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_DELETE) {
					removeTable(highlightedTable);
					highlightedTable = null;
					repaint();
				}
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			Log.debug(RoomEditor.class, "keyTyped, Mode: " + mode);
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

			Table table = new Table(UUID.randomUUID(), new RelativePoint(UUID.randomUUID(), x, y), 2, "");
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

		private int getStudentIndexToHighlight(int xClicked, int yClicked) {
			for (Table t : tables) {
				for (int i = 0; i < t.getNumberOfPlaces(); i++) {
					int x = RelativePoint.relativeToAbsoluteX(t.getPosition().getX(), getWidth(), (int) (PADDING * getWidth()));
					int y = RelativePoint.relativeToAbsoluteY(t.getPosition().getY(), getHeight(), (int) (PADDING * getHeight()));

					y = y - STUDENT_IMAGES_SPACING - NT.STUDENT_IMAGE_HEIGHT;
					int spacing = STUDENT_IMAGES_SPACING * i;
					if (i == 0) {
						spacing = 0;
					}
					x = x + (i * NT.STUDENT_IMAGE_WIDTH + spacing);

					if (FloatingPointUtil.isInBetween(xClicked, x, x + NT.STUDENT_IMAGE_WIDTH) && FloatingPointUtil.isInBetween(yClicked, y, y + NT.STUDENT_IMAGE_HEIGHT)) {
						highlightedTable = t;
						return i;
					}
				}
			}
			return -1;
		}

		private void showStudentInfo(int x, int y, Student student) {
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime studentTooltipLastSeen = studentsToLastTooltipShown.get(student);
			if (studentsToLastTooltipShown.get(student) != null && ShowToast.getMsDifferenceBetweenDates(studentTooltipLastSeen, now) < 5000) {
				return;
			}
			studentsToLastTooltipShown.put(student, LocalDateTime.now());
			String text = student.getFirstName() + " " + student.getLastName();
			ShowToast.showToastStatic(text, 3000, x, y + 5, ColorStore.BACKGROUND_DIALOG, 12);
		}
	}
}
