package view.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableRowSorter;

import console.Log;
import model.AObject;
import model.LazyAObject;
import model.Model;
import nt.NT;
import view.img.ImageStore;
import view.itf.TableAction;
import view.l10n.L10n;

/**
 * Table utility class.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class GenericTable<T extends AObject> extends JComponent {

	private JTable t;
	private JTextField tf;
	private TableModel tm;
	private TableRowSorter<TableModel> sorter;
	private boolean initDone = false;

	public GenericTable(List<T> items, List<String> columnNames, List<Class<?>> columnClasses, List<Method> columnGetters, List<Method> columnSetters, List<Integer> editableColumns, Map<Integer, TableAction> actions) {
		t = new JTable();
		tm = new TableModel(items, columnNames, columnClasses, columnGetters, columnSetters, editableColumns, actions);
		t.setModel(tm);
		JScrollPane scrollPane = new JScrollPane(t);

		GridBagLayout gridBagLayout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		setLayout(gridBagLayout);
		constraints.weightx = 1;
		constraints.weighty = 0.001;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.PAGE_START;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		tf = new HintTextField("🔍  " + L10n.getString("filterText"));
		tf.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				setFilter(tf.getText());
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		add(tf, constraints);

		constraints.gridy = 1;
		constraints.weighty = 1 - constraints.weighty;
		add(scrollPane, constraints);
	}

	public JTable getTable() {
		return t;
	}

	public TableModel getModel() {
		return tm;
	}

	public void fireTableDataChanged() {
		tm.fireTableDataChanged();
	}

	private void init() {
		if (initDone) {
			return;
		}
		initDone = true;

		t.setPreferredScrollableViewportSize(new Dimension(500, 70));
		t.setFillsViewportHeight(true);
		t.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		t.setRowSelectionAllowed(true);
		t.setColumnSelectionAllowed(true);
		t.setRowHeight(NT.STUDENT_IMAGE_HEIGHT + 10);

		setRenderersAndEditors();

		sorter = new TableRowSorter<>(tm);
		t.setRowSorter(sorter);
	}

	private void setRenderersAndEditors() {
		for (int i = 0; i < tm.columnClasses.size(); i++) {
			if (AObject.isByteArray(tm.columnClasses.get(i))) {
				t.getColumn(tm.columnNames.get(i)).setCellRenderer(new ImageRenderer());
				t.getColumn(tm.columnNames.get(i)).setMaxWidth(NT.STUDENT_IMAGE_WIDTH + 3);
			} else if (AObject.isLocalDate(tm.columnClasses.get(i))) {
				t.getColumn(tm.columnNames.get(i)).setCellRenderer(new DateRenderer());
				t.getColumn(tm.columnNames.get(i)).setCellEditor(new DateEditor());
			} else if (AObject.isList(tm.columnClasses.get(i))) {
				t.getColumn(tm.columnNames.get(i)).setCellRenderer(new ListRenderer());
			} else if (AObject.isLazyAObject(tm.columnClasses.get(i))) {
				t.getColumn(tm.columnNames.get(i)).setCellRenderer(new LazyAObjectRenderer());
			} else {
				t.getColumn(tm.columnNames.get(i)).setCellRenderer(new IconAndTextRenderer());
			}
		}
		for (Integer i : tm.actions.keySet()) {
			TableActionMouseListener ml = new TableActionMouseListener(tm.actions.get(i), i);
			if (((GenericRenderer) t.getColumn(tm.columnNames.get(i)).getCellRenderer()) instanceof GenericTable.IconAndTextRenderer) {
				IconAndTextRenderer r = ((IconAndTextRenderer) t.getColumn(tm.columnNames.get(i)).getCellRenderer());
				r.setMouseListener(ml);
				r.setTextToDisplay(tm.actions.get(i).getText());
				r.setIconToDisplay(tm.actions.get(i).getIcon());
				if (tm.actions.get(i).getText().isEmpty()) {
					t.getColumn(tm.columnNames.get(i)).setMaxWidth(NT.STUDENT_IMAGE_WIDTH + 3);
				}
			} else {
				GenericRenderer r = ((GenericRenderer) t.getColumn(tm.columnNames.get(i)).getCellRenderer());
				r.setMouseListener(ml);
				r.setIcon(ImageStore.getScaledImage(ImageStore.getImageIcon(tm.actions.get(i).getIcon()), NT.STUDENT_IMAGE_WIDTH, NT.STUDENT_IMAGE_HEIGHT));
			}
		}

	}

	private void setFilter(String filterText) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					List<Integer> indices = new ArrayList<>();
					for (int i = 0; i < tm.columnNames.size(); i++) {
						if (!tm.actions.keySet().contains(i)) {
							indices.add(i);
							Arrays.asList(1);
						}
					}
					int[] idx = new int[indices.size()];
					for (int i = 0; i < indices.size(); i++) {
						idx[i] = indices.get(i);
					}
					RowFilter<TableModel, Object> rf = RowFilter.regexFilter("(?i)" + filterText, idx);
					sorter.setRowFilter(rf);
				} catch (Exception e) {
					Log.error(GenericTable.class, "Could not set regex filter: " + e.getMessage());
				}
			}
		});
	}

	public class TableModel extends AbstractTableModel {
		private List<T> items;
		private List<String> columnNames = new ArrayList<>();
		private List<Class<?>> columnClasses = new ArrayList<>();
		private List<Method> columnGetters = new ArrayList<>();
		private List<Method> columnSetters = new ArrayList<>();
		private List<Integer> editableColumns = new ArrayList<>();
		private Map<Integer, TableAction> actions = new HashMap<>();

		public TableModel(List<T> items, List<String> columnNames, List<Class<?>> columnClasses, List<Method> columnGetters, List<Method> columnSetters, List<Integer> editableColumns, Map<Integer, TableAction> actions) {
			this.items = items;
			this.columnNames = columnNames;
			this.columnClasses = columnClasses;
			this.columnGetters = columnGetters;
			this.columnSetters = columnSetters;
			this.editableColumns = editableColumns;
			this.actions = actions;
		}

		@Override
		public String getColumnName(int column) {
			return columnNames.get(column);
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (columnIndex >= columnClasses.size()) {
				return Object.class;
			}
			return columnClasses.get(columnIndex);
		}

		@Override
		public int getColumnCount() {
			return columnNames.size();
		}

		@Override
		public int getRowCount() {
			return items.size();
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return editableColumns.contains(column);
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			init();
			if (columnIndex >= columnGetters.size()) {
				return null;
			}

			Method getter = columnGetters.get(columnIndex);
			T objectToGetFrom = items.get(rowIndex);
			try {
				return getter.invoke(objectToGetFrom);
			} catch (Exception e) {
				Log.error(GenericTable.class, "Could not invoke getter '" + getter + "' for object " + objectToGetFrom + "! " + e.getMessage());
			}
			return null;
		}

		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			if (columnIndex >= columnSetters.size()) {
				return;
			}

			Method setter = columnSetters.get(columnIndex);
			T objectToSetOn = items.get(rowIndex);
			try {
				setter.invoke(objectToSetOn, value);
			} catch (Exception e) {
				Log.error(GenericTable.class, "Could not invoke setter '" + setter + "' for object " + objectToSetOn + "! " + e.getMessage());
			}
			fireTableCellUpdated(rowIndex, columnIndex);
		}
	}

	private class GenericRenderer extends DefaultTableCellRenderer implements MouseListener {
		private MouseListener ml;

		public void setMouseListener(MouseListener ml) {
			this.ml = ml;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			ml.mouseClicked(e);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			ml.mouseEntered(e);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			ml.mouseExited(e);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			ml.mousePressed(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			ml.mouseReleased(e);
		}
	}

	private class DefaultRenderer extends GenericRenderer {
		@Override
		public void setValue(Object value) {
			setText(value.toString());
		}
	}

	private class DateRenderer extends GenericRenderer {
		@Override
		public void setValue(Object value) {
			if (AObject.isLocalDate(value)) {
				setText(NT.DF_FOR_DISPLAYING_DATE.format((LocalDate) value).toString());
			} else {
				Log.error(GenericTable.class, "DateRenderer: given object was not a date!");
			}
		}
	}

	private class ListRenderer extends GenericRenderer {
		@Override
		public void setValue(Object value) {
			if (AObject.isList(value)) {
				setText(L10n.getString("numberOfElements") + ": " + ((List<?>) value).size());
			} else {
				Log.error(GenericTable.class, "ListRenderer: given object was not a list!");
			}
		}
	}

	private class LazyAObjectRenderer extends GenericRenderer {
		@Override
		public void setValue(Object value) {
			if (AObject.isMissingAObject(value)) {
				if (t.getSelectedRow() >= 0 && t.getSelectedColumn() >= 0) {
					Object val = tm.getValueAt(t.convertRowIndexToModel(t.getSelectedRow()), t.convertColumnIndexToModel(t.getSelectedColumn()));
					if (AObject.isString(val)) {
						setText((String) val);
					} else {
						setText(L10n.getString("valueNotYetSet"));
					}
				} else {
					setText(L10n.getString("valueNotYetSet"));
				}
			} else if (AObject.isLazyAObject(value)) {
				setText(((AObject) Model.getInstance().expandLazyAObject((LazyAObject<?>) value)).getStringRepresentation());
			} else {
				Log.error(GenericTable.class, "ListRenderer: given object was not a LazyAObject!");
			}
		}
	}

	private class IconAndTextRenderer extends GenericRenderer {
		String icon;
		String text;

		private void setTextToDisplay(String text) {
			this.text = text;
		}

		private void setIconToDisplay(String icon) {
			this.icon = icon;
		}

		@Override
		public void setValue(Object value) {
			if (icon != null && !icon.isEmpty()) {
				setIcon(ImageStore.getScaledImage(ImageStore.getImageIcon(icon), NT.STUDENT_IMAGE_WIDTH, NT.STUDENT_IMAGE_HEIGHT));
			}

			if (text != null && !text.isEmpty()) {
				setText(text);
			} else if (value != null) {
				setText(value.toString());
			} else {
				setText("");
			}
		}
	}

	private class ImageRenderer extends IconAndTextRenderer {
		@Override
		public void setValue(Object value) {
			if (AObject.isByteArray(value)) {
				byte[] bytes = (byte[]) value;
				setIcon(new ImageIcon(ImageStore.createRGBImage(bytes, NT.STUDENT_IMAGE_WIDTH, NT.STUDENT_IMAGE_HEIGHT)));
			} else {
				Log.error(GenericTable.class, "IconRenderer: given object was not a byte array!");
			}
		}
	}

	private class DefaultEditor extends AbstractCellEditor implements TableCellEditor {
		JTextField textField = new JTextField();

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int vColIndex) {
			String s = value.toString();
			textField.setText(s);
			return textField;
		}

		@Override
		public Object getCellEditorValue() {
			return textField.getText();
		}
	}

	private class DateEditor extends AbstractCellEditor implements TableCellEditor {
		private JTextField textField = new JTextField();
		private LocalDate currentDate;

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int vColIndex) {
			currentDate = (LocalDate) value;
			String s = NT.DF_FOR_DISPLAYING_DATE.format(currentDate).toString();
			textField.setText(s);
			return textField;
		}

		@Override
		public Object getCellEditorValue() {
			String value = textField.getText();
			try {
				return NT.DF_FOR_DISPLAYING_DATE.parse((String) value);
			} catch (Exception e) {
				GenericDialog dialog = new GenericDialog(L10n.getString("wrongFormat"), Arrays.asList(new JLabel(LabelUtil.styleLabel(L10n.getString("pleaseEnterDateInFollowingFormat") + ": " + NT.FORMAT_FOR_DISPLAYING_DATE.toUpperCase()))), true);
				dialog.show();
				Log.info(GenericTable.class, "Could not parse date, probably wrong format, use: " + NT.FORMAT_FOR_DISPLAYING_DATE.toUpperCase() + "!");
			}
			return currentDate;
		}
	}

	private class TableActionMouseListener implements MouseListener {

		private TableAction action;
		private int column;

		private TableActionMouseListener(TableAction action, int column) {
			this.action = action;
			this.column = column;
			t.addMouseListener(this);
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			Log.debug(TableActionMouseListener.class, "mouseClicked()");
			if (action != null && t.getSelectedColumn() == column) {
				AObject selectedObject = tm.items.get(t.convertRowIndexToModel(t.getSelectedRow()));
				Log.debug(TableActionMouseListener.class, "selectedObject=" + selectedObject.getUuid() + ", action=" + action);
				action.setRowObject(selectedObject);
				action.run();
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			Log.debug(TableActionMouseListener.class, "mouseEntered()");
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			Log.debug(TableActionMouseListener.class, "mouseExited()");
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			Log.debug(TableActionMouseListener.class, "mousePressed()");
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			Log.debug(TableActionMouseListener.class, "mouseReleased()");
		}
	}
}
