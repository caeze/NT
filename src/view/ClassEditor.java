package view;

import console.Log;
import model.Student;

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
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import preferences.Preferences;
import view.img.ImageStore;
import view.itf.IViewComponent;
import view.util.ButtonUtil;
import view.util.ColorStore;

/**
 * Main Menu screen.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class ClassEditor implements IViewComponent {

	private static ClassEditor instance;

	private ClassEditorBoard editor = new ClassEditorBoard();
	private List<Table> tables = new ArrayList<>();

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
		NONE, ADD_TABLE, ADD_STUDENT;
	}

	private class Table {
		private static final int DEFAULT_WIDTH = 60;
		private static final int DEFAULT_HEIGHT = 20;
		private UUID uuid;
		private int width = DEFAULT_WIDTH;
		private int height = DEFAULT_HEIGHT;
		private int xPos;
		private int yPos;
		private List<Chair> chairs = new ArrayList<>();
		
		// TODO implement add chair fcn that takes into account with of table and other chairs
	}

	private class Chair {
		private static final int WIDTH = 20;
		private static final int HEIGHT = 20;
		private UUID uuid;
		private Table t;
		
		private int getXPos() {
			// calculate position corresponding to index in parent table
			return 10;
		}
		
		private int getYPos() {
			// calculate position corresponding to index in parent table
			return 10;
		}
		
		// private Student student;
	}

	private class ClassEditorBoard extends JPanel implements ComponentListener, MouseListener, MouseMotionListener, MouseWheelListener {

		private Mode mode = Mode.ADD_TABLE;

		private ClassEditorBoard() {
			addComponentListener(this);
			addMouseListener(this);
			addMouseMotionListener(this);
			addMouseWheelListener(this);
			setBackground(ColorStore.BACKGROUND_LIGHT);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			for (Table t : tables) {
				g.setColor(ColorStore.TABLE_COLOR);
				g.fillRect(t.xPos, t.yPos, t.width, t.height);
				g.setColor(ColorStore.CHAIR_COLOR);
				for (Chair c : t.chairs) {
					g.fillRect(c.getXPos(), c.getYPos(), Chair.WIDTH, Chair.HEIGHT);
				}
			}
			
			/*ImageIcon i = ImageStore.getScaledImage(ImageStore.getImageIcon("save.png"), STUDENT_IMAGE_WIDTH, STUDENT_IMAGE_HEIGHT);
    	byte[] im = ImageStore.getBytesFromImage(i);
    	String data = Base64.getEncoder().encodeToString(im);
    	System.out.println(data);
    	byte[] image =  Base64.getDecoder().decode(data);
    	
    	Student s = new Student(UUID.randomUUID(), "a", "b", new Date(), "email", "phone", image);
    	
        */
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
			if (Mode.ADD_TABLE.equals(mode)) {
				int x = e.getX();
				int y = e.getY();
				Chair c1 = new Chair();
				Chair c2 = new Chair();
				List<Chair> chairs = new ArrayList<>();
				chairs.add(c1);
				chairs.add(c2);
				Table t = new Table();
				t.xPos = x;
				t.yPos = y;
				t.chairs = chairs;
				tables.add(t);
			} else if (Mode.ADD_STUDENT.equals(mode)) {

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
