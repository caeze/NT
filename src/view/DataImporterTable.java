package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import console.Log;
import control.Control;
import filehandling.CsvFileHandlingUtil;
import model.AObject;
import model.Room;
import model.Student;
import util.DateUtil;
import util.ListUtil;
import util.UsefulRegexes;
import view.itf.IViewComponent;
import view.l10n.L10n;
import view.l10n.NamesDatabase;
import view.util.ButtonUtil;

/**
 * Data importer starter screen.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class DataImporterTable implements IViewComponent {

	private JPanel retComponent;

	private String dataToImport;
	private String cellSeparator;
	private String rowSeparator;
	private AObject objectToImport;
	private Map<Integer, Integer> columnIndexToAttributeIndex = new HashMap<>();

	private JTable table;

	public DataImporterTable(String dataToImport, String cellSeparator, String rowSeparator, Class<? extends AObject> classToImport) {
		this.dataToImport = dataToImport;
		this.cellSeparator = cellSeparator;
		this.rowSeparator = rowSeparator;
		try {
			for (Constructor<?> c : classToImport.getConstructors()) {
				if (c.getParameterCount() == 0) {
					objectToImport = (AObject) c.newInstance();
				}
			}
			if (objectToImport == null) {
				throw new Exception("Constructor not found!");
			}
		} catch (Exception e) {
			Log.error(DataImporterTable.class, "Error on finding object to import: " + e.getMessage());
		}
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
		JButton doneButton = ButtonUtil.createButton(new Runnable() {
			@Override
			public void run() {
				View.getInstance().popViewComponent(IViewComponent.Result.SAVE);
			}
		}, "done.png", 40, 40, L10n.getString("done"));
		retList.add(doneButton);
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
		constraints.weighty = 1;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.ipadx = 0;
		constraints.ipady = 0;

		List<List<String>> sanitizedFileContent = sanitizeFileContent();
		String[] columnNames = getMostLikelyHeaders(sanitizedFileContent);
		String[][] data = getDataArray(sanitizedFileContent);
		table = new JTable(data, columnNames);
		retComponent.add(new JScrollPane(table), constraints);

		return retComponent;
	}

	@Override
	public void uninitializeViewComponent() {
	}

	@Override
	public void resultFromLastViewComponent(IViewComponent component, Result result) {
	}

	private String[] getMostLikelyHeaders(List<List<String>> sanitizedFileContent) {
		int[] sizes = { sanitizedFileContent.get(0).size(), sanitizedFileContent.size() };
		String[][] transposedContent = (String[][]) Array.newInstance(String.class, sizes);
		for (int i = 0; i < sizes[1]; i++) {
			for (int j = 0; j < sizes[0]; j++) {
				transposedContent[j][i] = sanitizedFileContent.get(i).get(j);
			}
		}

		// ----> member ids of the object that is to be imported
		// |
		// | position in transposedContent of corresponding member
		// V
		boolean[][] columnMappingMatrix = getConfidenceMatrix(transposedContent);
		for (int i = 0; i < columnMappingMatrix.length; i++) {
			for (int j = 0; j < columnMappingMatrix[i].length; j++) {
				if (columnMappingMatrix[i][j]) {
					columnIndexToAttributeIndex.put(i, j);
				}
			}
		}

		List<String> memberNames = objectToImport.getMemberNamesWithoutNumbers();
		String[] retArray = (String[]) Array.newInstance(String.class, memberNames.size());
		for (int i = 0; i < retArray.length; i++) {
			retArray[i] = null;
		}
		for (Integer i : columnIndexToAttributeIndex.keySet()) {
			retArray[i] = memberNames.get(columnIndexToAttributeIndex.get(i));
		}

		memberNames.removeAll(Arrays.asList(retArray));
		for (int i = memberNames.size(); i < retArray.length; i++) {
			if (retArray[i] == null) {
				if (!memberNames.isEmpty()) {
					retArray[i] = memberNames.remove(0);
				} else {
					retArray[i] = "";
				}
			}
		}

		return retArray;
	}

	private boolean[][] getConfidenceMatrix(String[][] transposedContent) {
		int membersSize = objectToImport.getMembers().size();
		int[] confidenceMatrixSizes = { membersSize, membersSize };
		int[][] memberNumberToColumnMatrix = (int[][]) Array.newInstance(int.class, confidenceMatrixSizes);
		for (int i = 0; i < membersSize; i++) {
			for (int j = 0; j < membersSize; j++) {
				memberNumberToColumnMatrix[i][j] = 0;
			}
		}
		for (int i = 0; i < transposedContent.length; i++) {
			for (int j = 0; j < transposedContent[i].length; j++) {
				if (objectToImport instanceof Student) {
					// Student: firstName, lastName, dateOfBirth, email, mobilePhone
					if (NamesDatabase.getInstance().isFirstName(transposedContent[i][j])) {
						String name = "firstName";
						memberNumberToColumnMatrix[i][objectToImport.getNumberOfMember(name) - 1]++;
					} else if (NamesDatabase.getInstance().isLastName(transposedContent[i][j])) {
						String name = "lastName";
						memberNumberToColumnMatrix[i][objectToImport.getNumberOfMember(name) - 1]++;
					} else if (transposedContent[i][j].contains("@") && transposedContent[i][j].contains(".")) {
						String name = "email";
						memberNumberToColumnMatrix[i][objectToImport.getNumberOfMember(name) - 1]++;
					} else if (DateUtil.getInstance().getDate(transposedContent[i][j]) != null) {
						String name = "dateOfBirth";
						memberNumberToColumnMatrix[i][objectToImport.getNumberOfMember(name) - 1]++;
					} else if (UsefulRegexes.GERMAN_PHONE_NUMBER.matcher(transposedContent[i][j]).matches()) {
						String name = "mobilePhone";
						memberNumberToColumnMatrix[i][objectToImport.getNumberOfMember(name) - 1]++;
					}
				}
				if (objectToImport instanceof Room) {
					// Room: name
					if (!UsefulRegexes.FLOATING_POINT_NUMBER.matcher(transposedContent[i][j]).matches()) {
						String name = "email";
						memberNumberToColumnMatrix[i][objectToImport.getNumberOfMember(name) - 1]++;
					}
				}
			}
		}
		int[] maxValues = new int[membersSize];
		for (int i = 0; i < memberNumberToColumnMatrix.length; i++) {
			maxValues[i] = Arrays.stream(memberNumberToColumnMatrix[i]).max().getAsInt();
		}
		boolean[][] retArrayOfArrays = (boolean[][]) Array.newInstance(boolean.class, confidenceMatrixSizes);
		for (int i = 0; i < membersSize; i++) {
			for (int j = 0; j < membersSize; j++) {
				if (memberNumberToColumnMatrix[i][j] == maxValues[i] && maxValues[i] > 0) {
					retArrayOfArrays[i][j] = true;
				} else {
					retArrayOfArrays[i][j] = false;
				}
			}
		}
		return retArrayOfArrays;
	}

	private String[][] getDataArray(List<List<String>> sanitizedFileContent) {
		return new ListUtil<String>().listOfListsToArrayOfArrays(sanitizedFileContent, String.class);
	}

	private List<List<String>> sanitizeFileContent() {
		CsvFileHandlingUtil util = new CsvFileHandlingUtil();
		List<List<String>> fileContent = util.getFileContent(dataToImport, cellSeparator, rowSeparator);
		int maxLengthOfRow = 0;
		for (List<String> row : fileContent) {
			maxLengthOfRow = Math.max(row.size(), maxLengthOfRow);
		}
		for (List<String> row : fileContent) {
			for (int i = 0; i < maxLengthOfRow - row.size(); i++) {
				row.add("");
			}
		}
		return fileContent;
	}
}
