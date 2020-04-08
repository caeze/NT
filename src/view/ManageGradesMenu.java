package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import control.Control;
import view.itf.IViewComponent;
import view.l10n.L10n;
import view.util.ButtonUtil;
import view.util.ColorStore;
import view.util.GenericDialog;
import view.util.LabelUtil;

/**
 * Manage grades menu screen.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class ManageGradesMenu implements IViewComponent {

	@Override
	public List<JButton> getButtonsLeft() {
		List<JButton> retList = new ArrayList<>();
		JButton backButton = ButtonUtil.createButton(new Runnable() {
			@Override
			public void run() {
				View.getInstance().popViewComponent();
			}
		}, "back.png", 40, 40, L10n.getString("back"));
		retList.add(backButton);
		JButton saveButton = ButtonUtil.createButton(new Runnable() {
			@Override
			public void run() {
				boolean success = Control.getInstance().saveCurrentProject();
				if (!success) {
					GenericDialog failedDialog = new GenericDialog(L10n.getString("saveFailed"), Arrays.asList(new JLabel(LabelUtil.styleLabel(L10n.getString("saveFailed")))), true);
					failedDialog.show();
				}
			}
		}, "save.png", 40, 40, L10n.getString("save"));
		retList.add(saveButton);
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

		JButton manageGradesButton = ButtonUtil.createButton(new Runnable() {
			@Override
			public void run() {

			}
		}, "done.png", L10n.getString("manageGrades"), 18, ColorStore.BLACK, L10n.getString("manageGrades"));
		retComponent.add(manageGradesButton, constraints);

		constraints.gridy++;
		constraints.anchor = GridBagConstraints.NORTH;
		JButton addExamButton = ButtonUtil.createButton(new Runnable() {
			@Override
			public void run() {
				View.getInstance().pushViewComponent(new CoursesList());
			}
		}, "add.png", L10n.getString("addExam"), 18, ColorStore.BLACK, L10n.getString("addExam"));
		retComponent.add(addExamButton, constraints);

		return retComponent;
	}

	@Override
	public void uninitializeViewComponent() {
	}

	@Override
	public void resultFromLastViewComponent(IViewComponent component, Result result) {
	}
}
