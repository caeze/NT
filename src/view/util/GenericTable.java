package view.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
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
import nt.NT;
import view.img.ImageStore;
import view.l10n.L10n;

/**
 * Table utility class.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class GenericTable<T> extends JComponent {

	private JTable t;
	private JTextField tf;
	private TableModel tm;
	private TableRowSorter<TableModel> sorter;
	private boolean initDone = false;

	public GenericTable(List<T> items, List<String> columnNames, List<Class<?>> columnClasses, List<String> columnGetters, List<String> columnSetters, List<Integer> editableRows) {
		t = new JTable();
		tm = new TableModel(items, columnNames, columnClasses, columnGetters, columnSetters, editableRows);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				t.setModel(tm);
			}
		});
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
			if (isByteArray(tm.columnClasses.get(i))) {
				t.getColumn(tm.columnNames.get(i)).setCellRenderer(new IconRenderer());
				t.getColumn(tm.columnNames.get(i)).setMaxWidth(NT.STUDENT_IMAGE_WIDTH + 3);
			} else if (isDate(tm.columnClasses.get(i))) {
				t.getColumn(tm.columnNames.get(i)).setCellRenderer(new DateRenderer());
			}
		}

		for (int i = 0; i < tm.columnClasses.size(); i++) {
			if (isDate(tm.columnClasses.get(i))) {
				t.getColumn(tm.columnNames.get(i)).setCellEditor(new DateEditor());
			}
		}
	}

	private void setFilter(String filterText) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					int[] indices = new int[tm.columnNames.size()];
					for (int i = 0; i < tm.columnNames.size(); i++) {
						indices[i] = i;
					}
					RowFilter<TableModel, Object> rf = RowFilter.regexFilter("(?i)" + filterText, indices);
					sorter.setRowFilter(rf);
				} catch (Exception e) {
					Log.error(GenericTable.class, "Could not set regex filter: " + e.getMessage());
				}
			}
		});
	}

	private class TableModel extends AbstractTableModel {
		private List<T> items;
		private List<String> columnNames = new ArrayList<>();
		private List<Class<?>> columnClasses = new ArrayList<>();
		private List<String> columnGetters = new ArrayList<>();
		private List<String> columnSetters = new ArrayList<>();
		private List<Integer> editableRows = new ArrayList<>();

		public TableModel(List<T> items, List<String> columnNames, List<Class<?>> columnClasses, List<String> columnGetters, List<String> columnSetters, List<Integer> editableRows) {
			this.items = items;
			this.columnNames = columnNames;
			this.columnClasses = columnClasses;
			this.columnGetters = columnGetters;
			this.columnSetters = columnSetters;
			this.editableRows = editableRows;
		}

		@Override
		public String getColumnName(int column) {
			return columnNames.get(column);
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
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
			return editableRows.contains(row);
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			init();

			String getterName = columnGetters.get(columnIndex);
			T objectToGetFrom = items.get(rowIndex);
			try {
				Method m = objectToGetFrom.getClass().getMethod(getterName);
				return m.invoke(objectToGetFrom);
			} catch (Exception e) {
				Log.error(GenericTable.class, "Could not find getter '" + getterName + "' for object " + objectToGetFrom + "! " + e.getMessage());
			}
			return null;
		}

		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			String setterName = columnSetters.get(columnIndex);
			T objectToSetOn = items.get(rowIndex);
			try {
				Method m = objectToSetOn.getClass().getMethod(setterName, columnClasses.get(columnIndex));
				m.invoke(objectToSetOn, value);
			} catch (Exception e) {
				Log.error(GenericTable.class, "Could not find setter '" + setterName + "' for object " + objectToSetOn + "! " + e.getMessage());
			}
			fireTableCellUpdated(rowIndex, columnIndex);
		}
	}

	private class DefaultRenderer extends DefaultTableCellRenderer {
		public void setValue(Object value) {
			setText(value.toString());
		}
	}

	private class DateRenderer extends DefaultTableCellRenderer {
		public void setValue(Object value) {
			if (isDate(value)) {
				setText(NT.SDF_FOR_DISPLAYING_DATE_ONLY.format((Date) value).toString());
			} else {
				setText("OBJECT_NOT_A_DATE");
				Log.error(GenericTable.class, "DateRenderer: given object was not a date!");
			}
		}
	}

	private class ListRenderer extends DefaultTableCellRenderer {
		public void setValue(Object value) {
			if (isList(value)) {
				setText("List size: " + ((List<?>) value).size());
			} else {
				setText("OBJECT_NOT_A_LIST");
				Log.error(GenericTable.class, "ListRenderer: given object was not a list!");
			}
		}
	}

	private class IconRenderer extends DefaultTableCellRenderer {
		public void setValue(Object value) {
			if (isByteArray(value)) {
				byte[] bytes = (byte[]) value;
				setIcon(new ImageIcon(ImageStore.createRGBImage(bytes, NT.STUDENT_IMAGE_WIDTH, NT.STUDENT_IMAGE_HEIGHT)));
			} else {
				setText("OBJECT_NOT_A_BYTE_ARRAY");
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
		JTextField textField = new JTextField();

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int vColIndex) {
			String s = NT.SDF_FOR_DISPLAYING_DATE_ONLY.format((Date) value).toString();
			textField.setText(s);
			return textField;
		}

		@Override
		public Object getCellEditorValue() {
			String value = textField.getText();
			try {
				return NT.SDF_FOR_DISPLAYING_DATE_ONLY.parse((String) value);
			} catch (ParseException e) {
				Log.error(GenericTable.class, "Could not parse date, probably wrong format, use: " + NT.SDF_FOR_DISPLAYING_DATE_ONLY + "!");
			}
			return null;
		}
	}

	private boolean isString(Object obj) {
		if (obj != null && obj instanceof Class) {
			Class<?> c = (Class<?>) obj;
			return c.getName().equals(String.class.getName());
		}
		return obj != null && obj instanceof String;
	}

	private boolean isDate(Object obj) {
		if (obj != null && obj instanceof Class) {
			Class<?> c = (Class<?>) obj;
			return c.getName().equals(Date.class.getName());
		}
		return obj != null && obj instanceof Date;
	}

	private boolean isDouble(Object obj) {
		if (obj != null && obj instanceof Class) {
			Class<?> c = (Class<?>) obj;
			return c.getName().equals(Double.class.getName());
		}
		return obj != null && obj instanceof Double;
	}

	private boolean isInteger(Object obj) {
		if (obj != null && obj instanceof Class) {
			Class<?> c = (Class<?>) obj;
			return c.getName().equals(Integer.class.getName());
		}
		return obj != null && obj instanceof Integer;
	}

	private boolean isList(Object obj) {
		if (obj != null && obj instanceof Class) {
			Class<?> c = (Class<?>) obj;
			return c.getName().equals(List.class.getName());
		}
		return obj != null && obj instanceof List;
	}

	private boolean isByteArray(Object obj) {
		if (obj != null && obj instanceof Class) {
			Class<?> c = (Class<?>) obj;
			return c.isArray() && c.getComponentType() == byte.class;
		}
		return obj != null && obj.getClass().isArray() && obj.getClass().getComponentType() == byte.class;
	}
}
