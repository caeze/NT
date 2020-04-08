package view;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import control.Control;
import filehandling.CsvFileHandlingUtil;
import filehandling.FileHandlingUtil;
import filehandling.StructuredDataFileHandlingUtil;
import filehandling.XlsFileHandlingUtil;
import filehandling.XlsxFileHandlingUtil;
import model.AObject;
import model.Room;
import model.Student;
import view.itf.IViewComponent;
import view.l10n.L10n;
import view.l10n.NamesDatabase;
import view.util.ButtonUtil;
import view.util.ColorStore;
import view.util.HintTextArea;
import view.util.HintTextField;

/**
 * Data importer starter screen.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class DataImporterStarter implements IViewComponent {

	private JPanel retComponent;
	private JComboBox<String> rowSeparator;
	private JComboBox<String> cellSeparator;
	private JComboBox<String> selectClassToImport;
	private JTextArea pasteTextArea;
	private JButton startDataImport;

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
		constraints.weighty = 0.01;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 10;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.ipadx = 10;
		constraints.ipady = 0;
		JLabel explanationLabel = new JLabel(L10n.getString("dataImporterExplanation"));
		retComponent.add(explanationLabel, constraints);

		JButton openFile = ButtonUtil.createButton(new Runnable() {
			@Override
			public void run() {
				File f = FileHandlingUtil.getInstance().showOpenFileSelector("csv", "xls", "xlsx");
				if (f != null) {
					String extension = FileHandlingUtil.getInstance().getExtension(f.getAbsolutePath()).toLowerCase();
					StructuredDataFileHandlingUtil fileHandlingUtil = null;
					switch (extension) {
					case "csv":
						fileHandlingUtil = new CsvFileHandlingUtil();
						break;
					case "xls":
						fileHandlingUtil = new XlsFileHandlingUtil();
						break;
					case "xlsx":
						fileHandlingUtil = new XlsxFileHandlingUtil();
						break;
					}
					List<List<String>> fileContent = fileHandlingUtil.getFileContent(f);
					String content = "";
					for (List<String> row : fileContent) {
						String r = "";
						for (String cell : row) {
							if (!r.isEmpty()) {
								r += fileHandlingUtil.getCellSeparator();
							}
							r += cell;
						}
						if (!content.isEmpty()) {
							content += fileHandlingUtil.getRowSeparator();
						}
						content += r;
					}
					pasteTextArea.setText(content);
					String contentType = determineContentType(fileContent);
					cellSeparator.setSelectedItem(cellSepToL10n(fileHandlingUtil.getCellSeparator()));
					rowSeparator.setSelectedItem(rowSepToL10n(fileHandlingUtil.getRowSeparator()));
					selectClassToImport.setSelectedItem(contentType);
				}
			}
		}, "directory.png", L10n.getString("openFile"), 18, ColorStore.BLACK, L10n.getString("openFile"), 0.6666);
		constraints.weighty = 0.01;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.CENTER;
		retComponent.add(openFile, constraints);

		JLabel selectClassToImportLabel = new JLabel(L10n.getString("selectClassToImport"));
		constraints.gridx++;
		constraints.gridx++;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.EAST;
		retComponent.add(selectClassToImportLabel, constraints);
		selectClassToImport = new JComboBox<>();
		selectClassToImport.addItem(L10n.getString("autodetect"));
		selectClassToImport.addItem(L10n.getString("students"));
		selectClassToImport.addItem(L10n.getString("rooms"));
		constraints.gridx++;
		constraints.anchor = GridBagConstraints.WEST;
		selectClassToImport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedString = (String) selectClassToImport.getSelectedItem();
				startDataImport.setEnabled(!selectedString.equals(L10n.getString("autodetect")));
			}
		});
		retComponent.add(selectClassToImport, constraints);

		JLabel separatorLabel = new JLabel(L10n.getString("cellSeparator"));
		constraints.gridx++;
		constraints.anchor = GridBagConstraints.EAST;
		retComponent.add(separatorLabel, constraints);
		cellSeparator = new JComboBox<>();
		cellSeparator.addItem(L10n.getString("autodetect"));
		cellSeparator.addItem(L10n.getString("semicolon"));
		cellSeparator.addItem(L10n.getString("tab"));
		cellSeparator.addItem(L10n.getString("comma"));
		cellSeparator.addItem(L10n.getString("dot"));
		cellSeparator.addItem(L10n.getString("oneSpace"));
		cellSeparator.addItem(L10n.getString("oneSpaceOrMore"));
		cellSeparator.addItem(L10n.getString("twoSpacesOrMore"));
		constraints.gridx++;
		constraints.anchor = GridBagConstraints.WEST;
		retComponent.add(cellSeparator, constraints);

		JLabel newRowLabel = new JLabel(L10n.getString("rowSeparator"));
		constraints.gridx++;
		constraints.anchor = GridBagConstraints.EAST;
		retComponent.add(newRowLabel, constraints);
		rowSeparator = new JComboBox<>();
		rowSeparator.addItem(L10n.getString("autodetect"));
		rowSeparator.addItem(L10n.getString("nl"));
		rowSeparator.addItem(L10n.getString("cr"));
		rowSeparator.addItem(L10n.getString("nlcr"));
		rowSeparator.addItem(L10n.getString("crnl"));
		constraints.gridx++;
		constraints.anchor = GridBagConstraints.WEST;
		retComponent.add(rowSeparator, constraints);

		constraints.gridx++;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridwidth = 2;
		startDataImport = ButtonUtil.createButton(new Runnable() {
			@Override
			public void run() {
				if (!pasteTextArea.getText().isEmpty()) {
					String cellSeparatorChar = l10nToCellSep((String) cellSeparator.getSelectedItem());
					String rowSeparatorChar = l10nToRowSep((String) rowSeparator.getSelectedItem());
					Class<? extends AObject> classToImport = l10nToAObject((String) selectClassToImport.getSelectedItem());
					View.getInstance().pushViewComponent(new DataImporterTable(pasteTextArea.getText(), cellSeparatorChar, rowSeparatorChar, classToImport));
				}
			}
		}, "import.png", L10n.getString("startDataImport"), 18, ColorStore.BLACK, L10n.getString("startDataImport"), 0.6666);
		startDataImport.setEnabled(false);
		retComponent.add(startDataImport, constraints);

		constraints.weighty = 0.97;
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 10;
		constraints.fill = GridBagConstraints.BOTH;
		pasteTextArea = new HintTextArea(L10n.getString("orPasteExcelFileContentHere"));
		Font font = new Font("Monospaced", pasteTextArea.getFont().getStyle(), pasteTextArea.getFont().getSize());
		pasteTextArea.setFont(font);
		Action action = pasteTextArea.getActionMap().get("paste-from-clipboard");
		pasteTextArea.getActionMap().put("paste-from-clipboard", new CustomPasteAction(action));
		pasteTextArea.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				startDataImport.setEnabled(!pasteTextArea.getText().isEmpty());
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				startDataImport.setEnabled(!pasteTextArea.getText().isEmpty());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				startDataImport.setEnabled(!pasteTextArea.getText().isEmpty());
			}
		});

		JScrollPane scrollPane = new JScrollPane(pasteTextArea);
		retComponent.add(scrollPane, constraints);

		HintTextField regexTextField = new HintTextField(L10n.getString("regex"));
		regexTextField.setFont(font);
		constraints.weighty = 0.01;
		constraints.gridy = 3;
		constraints.gridwidth = 4;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		retComponent.add(regexTextField, constraints);

		HintTextField replaceTextField = new HintTextField(L10n.getString("replaceText"));
		replaceTextField.setFont(font);
		constraints.gridx++;
		constraints.gridx++;
		constraints.gridx++;
		constraints.gridx++;
		retComponent.add(replaceTextField, constraints);

		constraints.gridx++;
		constraints.gridx++;
		constraints.gridx++;
		constraints.gridx++;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridwidth = 1;
		JButton applyRegex = ButtonUtil.createButton(new Runnable() {
			@Override
			public void run() {
				if (!pasteTextArea.getText().isEmpty() && !regexTextField.getText().isEmpty()) {
					String text = pasteTextArea.getText().replaceAll(regexTextField.getText(), replaceTextField.getText());
					pasteTextArea.setText(text);
					pasteTextArea.repaint();
				}
			}
		}, "play.png", L10n.getString("applyRegex"), 18, ColorStore.BLACK, L10n.getString("applyRegex"), 0.6666);
		retComponent.add(applyRegex, constraints);

		return retComponent;
	}

	@Override
	public void uninitializeViewComponent() {
	}

	@Override
	public void resultFromLastViewComponent(IViewComponent component, Result result) {
	}

	private String cellSepToL10n(String inputSeparator) {
		if (";".equals(inputSeparator)) {
			return L10n.getString("semicolon");
		}
		if ("\t".equals(inputSeparator)) {
			return L10n.getString("tab");
		}
		if (",".equals(inputSeparator)) {
			return L10n.getString("comma");
		}
		if (".".equals(inputSeparator)) {
			return L10n.getString("dot");
		}
		if (" ".equals(inputSeparator)) {
			return L10n.getString("oneSpace");
		}
		if ("  *".matches(inputSeparator)) {
			return L10n.getString("oneSpaceOrMore");
		}
		if ("   *".matches(inputSeparator)) {
			return L10n.getString("twoSpacesOrMore");
		}

		return "";
	}

	private String l10nToCellSep(String selectedItem) {
		if (selectedItem.equals(L10n.getString("semicolon"))) {
			return ";";
		}
		if (selectedItem.equals(L10n.getString("tab"))) {
			return "\t";
		}
		if (selectedItem.equals(L10n.getString("comma"))) {
			return ",";
		}
		if (selectedItem.equals(L10n.getString("dot"))) {
			return ".";
		}
		if (selectedItem.equals(L10n.getString("oneSpace"))) {
			return " ";
		}
		if (selectedItem.equals(L10n.getString("oneSpaceOrMore"))) {
			return "  ";
		}
		if (selectedItem.equals(L10n.getString("twoSpacesOrMore"))) {
			return "   ";
		}
		return "";
	}

	private String rowSepToL10n(String inputSeparator) {
		if ("\n".equals(inputSeparator)) {
			return L10n.getString("nl");
		}
		if ("\r".equals(inputSeparator)) {
			return L10n.getString("cr");
		}
		if ("\n\r".equals(inputSeparator)) {
			return L10n.getString("nlcr");
		}
		if ("\r\n".equals(inputSeparator)) {
			return L10n.getString("crnl");
		}
		return "";
	}

	private String l10nToRowSep(String selectedItem) {
		if (selectedItem.equals(L10n.getString("nl"))) {
			return "\n";
		}
		if (selectedItem.equals(L10n.getString("cr"))) {
			return "\r";
		}
		if (selectedItem.equals(L10n.getString("nlcr"))) {
			return "\n\r";
		}
		if (selectedItem.equals(L10n.getString("crnl"))) {
			return "\r\n";
		}
		return "";
	}

	private String aObjectToL10n(Class<? extends AObject> c) {
		if (c.equals(Student.class)) {
			return L10n.getString("students");
		}
		if (c.equals(Room.class)) {
			return L10n.getString("rooms");
		}
		return "AObject";
	}

	private Class<? extends AObject> l10nToAObject(String selectedItem) {
		if (selectedItem.equals(L10n.getString("students"))) {
			return Student.class;
		}
		if (selectedItem.equals(L10n.getString("rooms"))) {
			return Room.class;
		}
		return AObject.class;
	}

	private String determineContentType(List<List<String>> fileContent) {
		for (List<String> row : fileContent) {
			for (String cell : row) {
				if (cell.contains("@")) {
					return aObjectToL10n(Student.class);
				}
				if (NamesDatabase.getInstance().isFirstName(cell)) {
					return aObjectToL10n(Student.class);
				}
				if (NamesDatabase.getInstance().isLastName(cell)) {
					return aObjectToL10n(Student.class);
				}
			}
		}
		return aObjectToL10n(Room.class);
	}

	private class CustomPasteAction extends AbstractAction {

		private Action action;

		public CustomPasteAction(Action action) {
			this.action = action;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			action.actionPerformed(e);

			CsvFileHandlingUtil fileHandlingUtil = new CsvFileHandlingUtil();
			List<List<String>> fileContent = fileHandlingUtil.getFileContent(pasteTextArea.getText());
			String contentType = determineContentType(fileContent);
			cellSeparator.setSelectedItem(cellSepToL10n(fileHandlingUtil.getCellSeparator()));
			rowSeparator.setSelectedItem(rowSepToL10n(fileHandlingUtil.getRowSeparator()));
			selectClassToImport.setSelectedItem(contentType);
		}
	}
}
