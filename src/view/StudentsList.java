package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import console.Log;
import control.Control;
import filehandling.FileHandlingUtil;
import model.Model;
import model.Student;
import nt.NT;
import view.img.ImageStore;
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
public class StudentsList implements IViewComponent {

	public enum Action {
		EDIT_STUDENTS, SELECT_STUDENT
	}

	private ImageChooser ic;
	private TableAction changeImageAction;
	private TableAction removeItemAction;
	private TableAction selectItemAction;
	private GenericTable<Student> studentsTable;
	private JPanel retComponent;
	private Student selectedStudent = null;
	private Action action;

	public StudentsList(Action action) {
		this.action = action;
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
		if (action.equals(Action.EDIT_STUDENTS)) {
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
					byte[] bytes = ImageStore.getBytesFromImage(ImageStore.getScaledImage(ImageStore.getImageIcon(""), NT.STUDENT_IMAGE_WIDTH, NT.STUDENT_IMAGE_HEIGHT));
					Student student = new Student(UUID.randomUUID(), "", "", LocalDate.now(), "", "", "", bytes);
					Model.getInstance().addStudent(student);
					studentsTable.fireTableDataChanged();
				}
			}, "add.png", 40, 40, L10n.getString("add"));
			retList.add(addButton);
		}
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
		if (action.equals(Action.EDIT_STUDENTS)) {
			retList.add(ButtonUtil.createButton("empty.png", 40, 40));
			retList.add(ButtonUtil.createButton("empty.png", 40, 40));
		}
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
		columnNames.addAll(Arrays.asList(L10n.getString("uuid"), L10n.getString("firstName"), L10n.getString("lastName"), L10n.getString("dateOfBirth"), L10n.getString("email"), L10n.getString("mobilePhone"), L10n.getString("comment"), L10n.getString("image")));
		List<Class<?>> columnClasses = new Student().getMembersTypes();
		List<Method> columnGetters = new Student().getGetters();
		List<Method> columnSetters = new Student().getSetters();
		List<Integer> editableColumns = new ArrayList<>();
		changeImageAction = new TableAction("edit.png", "") {
			@Override
			public void run() {
				Log.debug(StudentsList.class, "changeImageAction, selectedObject=" + getRowObject().getUuid());

				File f = FileHandlingUtil.getInstance().showOpenFileSelector("png");
				if (f != null) {
					ic = new ImageChooser(f);
					View.getInstance().pushViewComponent(ic);
				}
			}
		};
		removeItemAction = new TableAction("delete.png", "") {
			@Override
			public void run() {
				Log.debug(StudentsList.class, "removeItemAction, selectedObject=" + getRowObject().getUuid());

				List<String> options = new ArrayList<>();
				options.add(L10n.getString("remove"));
				options.add(L10n.getString("cancel"));
				GenericDialog dialog = new GenericDialog(L10n.getString("removeItem"), Arrays.asList(new JLabel(LabelUtil.styleLabel(L10n.getString("reallyRemoveItem")))), options);
				int selection = dialog.show();
				switch (selection) {
				case 0:
					Model.getInstance().removeAObject(getRowObject().getUuid());
					studentsTable.fireTableDataChanged();
					break;
				case 1:
				default:
					break;
				}
			}
		};
		selectItemAction = new TableAction("right.png", "") {
			@Override
			public void run() {
				Log.debug(StudentsList.class, "selectItemAction, selectedObject=" + getRowObject().getUuid());

				selectedStudent = (Student) getRowObject();
				View.getInstance().popViewComponent(IViewComponent.Result.SAVE);
			}
		};
		Map<Integer, TableAction> actions = new HashMap<>();
		if (action.equals(Action.EDIT_STUDENTS)) {
			editableColumns.addAll(Arrays.asList(1, 2, 3, 4, 5, 6));
			actions.put(7, changeImageAction);
			actions.put(8, removeItemAction);
			columnNames.add(L10n.getString("remove"));
			columnClasses.add(Object.class);
		} else {
			actions.put(8, selectItemAction);
			columnNames.add(L10n.getString("select"));
			columnClasses.add(Object.class);
		}
		studentsTable = new GenericTable<>(Model.getInstance().getAllStudents(), columnNames, columnClasses, columnGetters, columnSetters, editableColumns, actions);

		retComponent.add(studentsTable, constraints);

		return retComponent;
	}

	@Override
	public void uninitializeViewComponent() {
	}

	@Override
	public void resultFromLastViewComponent(IViewComponent component, Result result) {
		if (component.equals(ic)) {
			if (Result.SAVE.equals(result)) {
				if (ic.getSelectedCroppedImage() == null) {
					return;
				}
				Student rowObject = (Student) changeImageAction.getRowObject();
				ImageIcon imageIcon = ImageStore.getScaledImage(ic.getSelectedCroppedImage(), NT.STUDENT_IMAGE_WIDTH, NT.STUDENT_IMAGE_HEIGHT);
				byte[] image = ImageStore.getBytesFromImage(imageIcon);
				rowObject.setImage(image);
				studentsTable.fireTableDataChanged();
			} else if (Result.CANCEL.equals(result)) {
			}
		}
	}

	public Student getSelectedStudent() {
		return selectedStudent;
	}
}
