package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import console.Log;
import control.Control;
import model.Model;
import preferences.Preferences;
import view.img.ImageStore;
import view.itf.IViewComponent;
import view.l10n.L10n;
import view.util.ButtonUtil;
import view.util.ColorStore;

/**
 * View of the MVC pattern.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class View extends JFrame {

	private static View instance;

	private JPanel menuLeftPanel;
	private JPanel menuCenterPanel;
	private JPanel menuRightPanel;
	private JPanel viewContentPanel;
	private boolean alreadyInitialized = false;

	private List<IViewComponent> viewComponentStack = new ArrayList<>();

	private View() {
		// hide constructor, singleton pattern
		super(L10n.getString("NT"));
	}

	/**
	 * Get an instance, singleton pattern.
	 *
	 * @return an instance
	 */
	public static View getInstance() {
		if (instance == null) {
			instance = new View();
			instance.init();
		}
		return instance;
	}

	private void init() {
		try {
			ImageIcon icon = ImageStore.getImageIcon("logo.png");
			setIconImage(icon.getImage());
		} catch (Exception e) {
			Log.error(View.class, e.getMessage());
		}

		GridBagLayout gridBagLayout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();

		JPanel contentPanel = new JPanel(gridBagLayout);

		JPanel menuPanel = new JPanel(gridBagLayout);
		menuLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		menuLeftPanel.setBackground(ColorStore.BACKGROUND_DARK);
		constraints.weightx = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridwidth = 1;
		menuPanel.add(menuLeftPanel, constraints);
		menuCenterPanel = new JPanel(new GridLayout(1, 1));
		menuCenterPanel.setBackground(ColorStore.BACKGROUND_DARK);
		constraints.gridwidth = 2;
		menuPanel.add(menuCenterPanel, constraints);
		menuRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		menuRightPanel.setBackground(ColorStore.BACKGROUND_DARK);
		constraints.gridwidth = 1;
		menuPanel.add(menuRightPanel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.PAGE_START;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weighty = 0;
		constraints.weightx = 1;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		contentPanel.add(menuPanel, constraints);

		JPanel colorBannerPanel = new JPanel(gridBagLayout);
		colorBannerPanel.setBackground(ColorStore.BACKGROUND_NORMAL);
		JButton emptyButton1 = ButtonUtil.createButton("empty.png", 10, 10);
		colorBannerPanel.add(emptyButton1);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		contentPanel.add(colorBannerPanel, constraints);

		viewContentPanel = new JPanel(gridBagLayout);
		viewContentPanel.setBackground(ColorStore.BACKGROUND);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.PAGE_START;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1;
		constraints.weighty = 1;
		contentPanel.add(viewContentPanel, constraints);

		instance.add(contentPanel);

		// own exit handler
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Control.getInstance().exitProgram();
			}
		});

		if (!alreadyInitialized) {
			alreadyInitialized = true;
			setSize(900, 600);
			setMinimumSize(getSize());
			setLocationRelativeTo(null);
			setExtendedState(JFrame.MAXIMIZED_BOTH);
			setVisible(true);
			pack();
		}
	}

	public void pushViewComponent(IViewComponent componentToSet) {
		viewComponentStack.add(0, componentToSet);
		setContent(componentToSet, true);
	}

	public IViewComponent popViewComponent() {
		return popViewComponent(IViewComponent.Result.NONE);
	}

	public IViewComponent popViewComponent(IViewComponent.Result action) {
		if (IViewComponent.Result.SAVE.equals(action)) {
			Model.getInstance().storeSnapshot();
		}
		if (viewComponentStack.size() < 2) {
			Control.getInstance().exitProgram();
		}
		IViewComponent c = viewComponentStack.remove(0);
		c.uninitializeViewComponent();
		setContent(viewComponentStack.get(0), false);
		viewComponentStack.get(0).resultFromLastViewComponent(c, action);
		return c;
	}

	private void setContent(IViewComponent componentToSet, boolean firstInitialization) {
		menuLeftPanel.removeAll();
		menuCenterPanel.removeAll();
		menuRightPanel.removeAll();
		viewContentPanel.removeAll();

		List<JButton> buttonsLeft = componentToSet.getButtonsLeft();
		List<JButton> buttonsRight = componentToSet.getButtonsRight();
		while (buttonsLeft.size() < buttonsRight.size()) {
			buttonsLeft.add(ButtonUtil.createButton("empty.png", 40, 40));
		}
		while (buttonsLeft.size() > buttonsRight.size()) {
			buttonsRight.add(0, ButtonUtil.createButton("empty.png", 40, 40));
		}
		for (JButton button : buttonsLeft) {
			menuLeftPanel.add(button);
		}
		JPanel menuCenterInnerPanel = new JPanel();
		menuCenterInnerPanel.setLayout(new GridLayout(1, 1));
		menuCenterInnerPanel.setBackground(ColorStore.BACKGROUND_DARK);
		for (JComponent component : componentToSet.getComponentsCenter()) {
			menuCenterInnerPanel.add(component, BorderLayout.SOUTH);
		}
		menuCenterPanel.add(menuCenterInnerPanel, BorderLayout.SOUTH);
		for (JButton button : buttonsRight) {
			menuRightPanel.add(button);
		}

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.PAGE_START;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		JComponent c = componentToSet.initializeViewComponent(firstInitialization);
		c.setBackground(ColorStore.BACKGROUND_LIGHT);
		viewContentPanel.add(c, constraints);

		repaint();
		revalidate();
	}

	public Dimension getContentViewSize() {
		return viewContentPanel.getSize();
	}

	public void setBackgroundColor(Color c) {
		viewContentPanel.setBackground(c);
	}

	public JButton getLogoButtonForTopCenter() {
		JButton iconButton = ButtonUtil.createButton(new Runnable() {
			@Override
			public void run() {
				try {
					if (Desktop.isDesktopSupported()) {
						String s = Preferences.getInstance().projectLocation;
						Desktop.getDesktop().browse(new URI(s.substring(0, s.lastIndexOf("/") + 1)));
					}
				} catch (Exception e) {
					Log.error(StartMenu.class, e.getMessage());
				}
			}
		}, "logo.png", 48, 48, 0.3);
		iconButton.setSelected(true);
		return iconButton;
	}
}
