package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import control.Control;
import view.itf.IViewComponent;
import view.l10n.L10n;
import view.util.ButtonUtil;
import view.util.ColorStore;

/**
 * Start Menu screen.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class StartMenu implements IViewComponent {

	JButton loadLastOpenedProjectButton = null;

	@Override
	public List<JButton> getButtonsRight() {
		List<JButton> retList = new ArrayList<>();
		JButton helpMenuButton = ButtonUtil.createButton(new Runnable() {
			@Override
			public void run() {
				View.getInstance().pushViewComponent(new Help());
			}
		}, "help.png", 40, 40, L10n.getString("help"));
		retList.add(helpMenuButton);
		JButton settingsMenuButton = ButtonUtil.createButton(new Runnable() {
			@Override
			public void run() {
				View.getInstance().pushViewComponent(new Settings());
			}
		}, "preferences.png", 40, 40, L10n.getString("preferences"));
		retList.add(settingsMenuButton);
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
	public List<JComponent> getComponentsCenter() {
		List<JComponent> retList = new ArrayList<>();
		retList.add(View.getInstance().getLogoButtonForTopCenter());
		return retList;
	}

	@Override
	public List<JButton> getButtonsLeft() {
		List<JButton> retList = new ArrayList<>();
		return retList;
	}

	@Override
	public JComponent initializeViewComponent(boolean firstInitialization) {
		JComponent retComponent = new JPanel();
		retComponent.setBackground(ColorStore.BACKGROUND);
		GridBagLayout gridBagLayout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		retComponent.setLayout(gridBagLayout);

		constraints.weightx = 0;
		constraints.weighty = 0.3333;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.fill = GridBagConstraints.CENTER;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.ipadx = 0;
		constraints.ipady = 0;

		loadLastOpenedProjectButton = ButtonUtil.createButton(new Runnable() {
			@Override
			public void run() {
				if (Control.getInstance().loadLastOpenedProject()) {
					View.getInstance().pushViewComponent(new MainMenu());
				} else {
					loadLastOpenedProjectButton.setEnabled(Control.getInstance().lastOpenedProjectExists());
				}
			}
		}, "reload.png", L10n.getString("loadLastOpenedProject"), 18, ColorStore.BLACK, L10n.getString("loadLastOpenedProject"));
		loadLastOpenedProjectButton.setEnabled(Control.getInstance().lastOpenedProjectExists());
		retComponent.add(loadLastOpenedProjectButton, constraints);

		constraints.gridy = 1;
		JButton loadProjectFromDiskButton = ButtonUtil.createButton(new Runnable() {
			@Override
			public void run() {
				boolean success = Control.getInstance().loadProjectFromDisk();
				if (success) {
					View.getInstance().pushViewComponent(new MainMenu());
				}
			}
		}, "directory.png", L10n.getString("loadProjectFromDisk"), 18, ColorStore.BLACK, L10n.getString("loadProjectFromDisk"));
		retComponent.add(loadProjectFromDiskButton, constraints);

		constraints.gridy = 2;
		JButton loadEmptyProjectButton = ButtonUtil.createButton(new Runnable() {
			@Override
			public void run() {
				boolean success = Control.getInstance().loadEmptyProject();
				if (success) {
					View.getInstance().pushViewComponent(new MainMenu());
				}
			}
		}, "new.png", L10n.getString("loadEmptyProject"), 18, ColorStore.BLACK, L10n.getString("loadEmptyProject"));
		retComponent.add(loadEmptyProjectButton, constraints);

		return retComponent;
	}

	@Override
	public void uninitializeViewComponent() {
	}

	@Override
	public void resultFromLastViewComponent(IViewComponent component, Result result) {
	}
}
