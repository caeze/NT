package view;

import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import console.Log;
import control.Control;
import model.Course;
import model.MissingAObject;
import model.Model;
import model.Room;
import preferences.Preferences;
import view.itf.IViewComponent;
import view.itf.TableAction;
import view.l10n.L10n;
import view.util.ButtonUtil;
import view.util.GenericDialog;
import view.util.GenericTable;
import view.util.LabelUtil;

/**
 * List of Students screen.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class CoursesList implements IViewComponent {

	private RoomEditor re;
	private TableAction classEditorAction;
	private TableAction removeItemAction;
	private GenericTable<Course> coursesTable;
	private JPanel retComponent;

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
		JButton addButton = ButtonUtil.createButton(new Runnable() {
			@Override
			public void run() {
				Course course = new Course(UUID.randomUUID(), "", "", "", new MissingAObject<Room>(), new ArrayList<>(), new ArrayList<>(), "");
				Model.getInstance().addCourse(course);
				coursesTable.fireTableDataChanged();
			}
		}, "add.png", 40, 40, L10n.getString("add"));
		retList.add(addButton);
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
					Log.error(CoursesList.class, e.getMessage());
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
		retList.add(ButtonUtil.createButton("empty.png", 40, 40));
		retList.add(ButtonUtil.createButton("empty.png", 40, 40));
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
		if (!firstInitialization) {
			return retComponent;
		}
		GridBagLayout gridBagLayout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();

		retComponent = new JPanel(gridBagLayout);
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.PAGE_START;
		constraints.ipadx = 0;
		constraints.ipady = 0;

		List<String> columnNames = new ArrayList<>();
		columnNames.addAll(Arrays.asList(L10n.getString("uuid"), L10n.getString("subject"), L10n.getString("grade"), L10n.getString("letter"), L10n.getString("room"), L10n.getString("students"), L10n.getString("studentOnTableMapping"), L10n.getString("comment")));
		List<Class<?>> columnClasses = new Course().getMembersTypes();
		List<Method> columnGetters = new Course().getGetters();
		List<Method> columnSetters = new Course().getSetters();
		List<Integer> editableColumns = Arrays.asList(1, 2, 3, 7);
		classEditorAction = new TableAction("edit.png", "") {
			@Override
			public void run() {
				re = new RoomEditor(RoomEditor.Action.EDIT_STUDENTS, ((Room) getRowObject()).getTables());
				View.getInstance().pushViewComponent(re);
			}
		};
		removeItemAction = new TableAction("delete.png", "") {
			@Override
			public void run() {
				Log.debug(CoursesList.class, "selectedObject=" + getRowObject().getUuid());

				List<String> options = new ArrayList<>();
				options.add(L10n.getString("remove"));
				options.add(L10n.getString("cancel"));
				GenericDialog dialog = new GenericDialog(L10n.getString("removeItem"), Arrays.asList(new JLabel(LabelUtil.styleLabel(L10n.getString("reallyRemoveItem")))), options);
				int selection = dialog.show();
				switch (selection) {
				case 0:
					Model.getInstance().removeAObject(getRowObject().getUuid());
					coursesTable.fireTableDataChanged();
					break;
				case 1:
				default:
					break;
				}
			}
		};
		Map<Integer, TableAction> actions = new HashMap<>();
		actions.put(8, classEditorAction);
		actions.put(9, removeItemAction);
		columnNames.add(L10n.getString("edit"));
		columnNames.add(L10n.getString("remove"));
		columnClasses.add(Object.class);
		columnClasses.add(Object.class);
		coursesTable = new GenericTable<>(Model.getInstance().getAllCourses(), columnNames, columnClasses, columnGetters, columnSetters, editableColumns, actions);

		retComponent.add(coursesTable, constraints);

		return retComponent;
	}

	@Override
	public void uninitializeViewComponent() {
	}

	@Override
	public void resultFromLastViewComponent(IViewComponent component, Result result) {
		if (component.equals(re)) {
			if (Result.SAVE.equals(result)) {
				Room rowObject = (Room) classEditorAction.getRowObject();
				rowObject.setTables(re.getTables());
				coursesTable.fireTableDataChanged();
			} else if (Result.CANCEL.equals(result)) {
			}
		}
	}
}
