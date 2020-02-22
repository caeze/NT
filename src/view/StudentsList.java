package view;

import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import console.Log;
import model.Student;
import preferences.Preferences;
import view.itf.IViewComponent;
import view.l10n.L10n;
import view.util.ButtonUtil;
import view.util.GenericTable;

/**
 * List of Students screen.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class StudentsList implements IViewComponent {

	private static StudentsList instance;

	private StudentsList() {
		// hide constructor, singleton pattern
	}

	/**
	 * Get an instance, singleton pattern.
	 *
	 * @return an instance
	 */
	public static StudentsList getInstance() {
		if (instance == null) {
			instance = new StudentsList();
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
					Log.error(StudentsList.class, e.getMessage());
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

		List<Student> items = new ArrayList<>();
		items.add(Student.getDummyStudent());
		items.add(Student.getDummyStudent());
		items.add(Student.getDummyStudent());
		items.add(Student.getDummyStudent());
		items.add(Student.getDummyStudent());
		items.add(Student.getDummyStudent());
		items.add(Student.getDummyStudent());
		items.add(Student.getDummyStudent());
		items.add(Student.getDummyStudent());
		items.add(Student.getDummyStudent());
		items.add(Student.getDummyStudent());
		items.add(Student.getDummyStudent());
		items.add(Student.getDummyStudent());
		items.add(Student.getDummyStudent());
		items.add(Student.getDummyStudent());
		items.add(Student.getDummyStudent());
		items.add(Student.getDummyStudent());
		items.add(Student.getDummyStudent());
		items.add(Student.getDummyStudent());
		items.add(Student.getDummyStudent());
		items.add(Student.getDummyStudent());
		items.add(Student.getDummyStudent());
		List<String> columnNames = Arrays.asList(L10n.getString("uuid"), L10n.getString("firstName"), L10n.getString("lastName"), L10n.getString("dateOfBirth"), L10n.getString("email"), L10n.getString("mobilePhone"), L10n.getString("comment"), L10n.getString("image"));
		List<Class<?>> columnClasses = items.get(0).getMembersTypes();
		List<String> columnGetters = items.get(0).getGetters();
		List<String> columnSetters = items.get(0).getSetters();
		List<Integer> editableRows = Arrays.asList(0, 1, 2, 3, 4, 5, 6);

		final GenericTable<Student> studentsTable = new GenericTable<>(items, columnNames, columnClasses, columnGetters, columnSetters, editableRows);
		retComponent.add(studentsTable, constraints);

		return retComponent;
	}

	@Override
	public void uninitializeViewComponent() {
		instance = null;
	}
}
