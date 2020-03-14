package view;

import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import console.Log;
import control.Control;
import model.Model;
import preferences.Preferences;
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
		retList.add(ButtonUtil.createButton("empty.png", 40, 40));
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
					Log.error(MainMenu.class, e.getMessage());
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
				Date now = new Date();
				Calendar c = new GregorianCalendar();
				c.setTime(now);
				for (int i = -5; i < 5; i++) {
					String year = (c.get(Calendar.YEAR) + i) + "/" + (c.get(Calendar.YEAR) + 1 + i);
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

		constraints.gridy = 1;
		JButton manageGradesButton = ButtonUtil.createButton(new Runnable() {
			@Override
			public void run() {

			}
		}, "done.png", L10n.getString("manageGrades"), 18, ColorStore.BLACK, L10n.getString("manageGrades"));
		retComponent.add(manageGradesButton, constraints);

		constraints.gridy = 2;
		JButton manageCoursesButton = ButtonUtil.createButton(new Runnable() {
			@Override
			public void run() {
				View.getInstance().pushViewComponent(new CoursesList());
			}
		}, "right.png", L10n.getString("manageCourses"), 18, ColorStore.BLACK, L10n.getString("manageCourses"));
		retComponent.add(manageCoursesButton, constraints);

		constraints.gridy = 3;
		JButton manageStudentsButton = ButtonUtil.createButton(new Runnable() {
			@Override
			public void run() {
				View.getInstance().pushViewComponent(new StudentsList(false));
			}
		}, "persons.png", L10n.getString("manageStudents"), 18, ColorStore.BLACK, L10n.getString("manageStudents"));
		retComponent.add(manageStudentsButton, constraints);

		constraints.gridy = 4;
		JButton manageRoomsButton = ButtonUtil.createButton(new Runnable() {
			@Override
			public void run() {
				View.getInstance().pushViewComponent(new RoomsList());
			}
		}, "home.png", L10n.getString("manageRooms"), 18, ColorStore.BLACK, L10n.getString("manageRooms"));
		retComponent.add(manageRoomsButton, constraints);

		return retComponent;
	}

	@Override
	public void uninitializeViewComponent() {
	}

	@Override
	public void resultFromLastViewComponent(IViewComponent component, Result result) {
	}
}
