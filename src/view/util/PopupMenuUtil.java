package view.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * Popup menu utility class.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class PopupMenuUtil {

	List<JPopupMenu> menus = new ArrayList<>();

	private static PopupMenuUtil instance;

	private PopupMenuUtil() {
		// hide constructor, singleton pattern
	}

	/**
	 * Get an instance, singleton pattern.
	 *
	 * @return an instance
	 */
	public static PopupMenuUtil getInstance() {
		if (instance == null) {
			instance = new PopupMenuUtil();
		}
		return instance;
	}

	public JPopupMenu showPopupMenu(int x, int y, Map<String, Runnable> elements) {
		JPopupMenu menu = new PopupMenu(x, y, elements);
		menus.add(menu);
		return menu;
	}

	public void killAllPopupMenus() {
		for (JPopupMenu m : menus) {
			m.setVisible(false);
		}
		menus.clear();
	}

	public class PopupMenu extends JPopupMenu {
		public PopupMenu(int x, int y, Map<String, Runnable> elements) {
			for (String s : elements.keySet()) {
				JMenuItem item = new JMenuItem(s);
				item.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						elements.get(s).run();
						setVisible(false);
					}
				});
				add(item);
			}
			setLocation(x, y);
			pack();
			setVisible(true);
		}
	}
}
