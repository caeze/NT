package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import control.Control;
import model.Model;
import view.itf.IViewComponent;
import view.l10n.L10n;
import view.util.ButtonUtil;
import view.util.ColorStore;
import view.util.GenericDialog;
import view.util.LabelUtil;

/**
 * Main Menu screen.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class MainMenu implements IViewComponent {

	JButton currentYearButton;
	JButton undoButton = null;
	JButton redoButton = null;

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
		undoButton = ButtonUtil.createButton(new Runnable() {
			@Override
			public void run() {
				undoButton.setEnabled(Model.getInstance().canUndo());
				Model.getInstance().undo();
				View.getInstance().repaint();
			}
		}, "revert.png", 40, 40, L10n.getString("undo"));
		retList.add(undoButton);
		undoButton.setEnabled(Model.getInstance().canUndo());
		redoButton = ButtonUtil.createButton(new Runnable() {
			@Override
			public void run() {
				redoButton.setEnabled(Model.getInstance().canRedo());
				Model.getInstance().redo();
				View.getInstance().repaint();
			}
		}, "reload.png", 40, 40, L10n.getString("redo"));
		retList.add(redoButton);
		redoButton.setEnabled(Model.getInstance().canRedo());
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

		currentYearButton = ButtonUtil.createButton(new Runnable() {
			@Override
			public void run() {
				List<String> options = new ArrayList<>();
				options.add(L10n.getString("ok"));
				options.add(L10n.getString("cancel"));
				JLabel explanation = new JLabel(LabelUtil.styleLabel(L10n.getString("selectYear")));
				JComboBox<String> years = new JComboBox<>();
				LocalDate now = LocalDate.now();
				int offset = 10;
				for (int i = -offset / 2; i < offset; i++) {
					String year = (now.getYear() + i) + "/" + (now.getYear() + 1 + i);
					years.addItem(year);
				}
				years.setSelectedItem(Model.getInstance().getCurrentProject().getYear());
				GenericDialog dialog = new GenericDialog(L10n.getString("selectYear"), Arrays.asList(explanation, years), options);
				int selection = dialog.show();
				switch (selection) {
				case 0:
					Model.getInstance().getCurrentProject().setYear((String) years.getSelectedItem());
					ButtonUtil.updateButton(currentYearButton, "star.png", L10n.getString("term") + ": " + Model.getInstance().getCurrentProject().getYear(), 18, ColorStore.BLACK, L10n.getString("editYear"));
					View.getInstance().repaint();
					break;
				case 1:
				default:
					break;
				}
			}
		}, "star.png", L10n.getString("term") + ": " + Model.getInstance().getCurrentProject().getYear(), 18, ColorStore.BLACK, L10n.getString("editYear"));
		retComponent.add(currentYearButton, constraints);

		constraints.gridy++;
		JButton manageGradesButton = ButtonUtil.createButton(new Runnable() {
			@Override
			public void run() {
				View.getInstance().pushViewComponent(new ManageGradesMenu());
			}
		}, "done.png", L10n.getString("manageGrades"), 18, ColorStore.BLACK, L10n.getString("manageGrades"));
		retComponent.add(manageGradesButton, constraints);

		constraints.gridy++;
		JButton manageCoursesButton = ButtonUtil.createButton(new Runnable() {
			@Override
			public void run() {
				View.getInstance().pushViewComponent(new CoursesList());
			}
		}, "right.png", L10n.getString("manageCourses"), 18, ColorStore.BLACK, L10n.getString("manageCourses"));
		retComponent.add(manageCoursesButton, constraints);

		constraints.gridy++;
		JButton manageStudentsButton = ButtonUtil.createButton(new Runnable() {
			@Override
			public void run() {
				View.getInstance().pushViewComponent(new StudentsList(StudentsList.Action.EDIT_STUDENTS));
			}
		}, "persons.png", L10n.getString("manageStudents"), 18, ColorStore.BLACK, L10n.getString("manageStudents"));
		retComponent.add(manageStudentsButton, constraints);

		constraints.gridy++;
		JButton manageRoomsButton = ButtonUtil.createButton(new Runnable() {
			@Override
			public void run() {
				View.getInstance().pushViewComponent(new RoomsList(RoomsList.Action.EDIT_ROOMS));
			}
		}, "home.png", L10n.getString("manageRooms"), 18, ColorStore.BLACK, L10n.getString("manageRooms"));
		retComponent.add(manageRoomsButton, constraints);

		constraints.gridy++;
		JButton importDataButton = ButtonUtil.createButton(new Runnable() {
			@Override
			public void run() {
				View.getInstance().pushViewComponent(new DataImporterStarter());
			}
		}, "import.png", L10n.getString("importData"), 18, ColorStore.BLACK, L10n.getString("manageRooms"));
		retComponent.add(importDataButton, constraints);

		return retComponent;
	}

	@Override
	public void uninitializeViewComponent() {
	}

	@Override
	public void resultFromLastViewComponent(IViewComponent component, Result result) {
	}
}
