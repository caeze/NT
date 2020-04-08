package filehandling;

import java.io.File;
import java.util.List;

/**
 * Structured data file handling util.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public abstract class StructuredDataFileHandlingUtil {

	protected String cellSeparator;
	protected String rowSeparator;
	protected String quoteChar;

	public String getCellSeparator() {
		return cellSeparator;
	}

	public void setCellSeparator(String cellSeparator) {
		this.cellSeparator = cellSeparator;
	}

	public String getRowSeparator() {
		return rowSeparator;
	}

	public void setRowSeparator(String rowSeparator) {
		this.rowSeparator = rowSeparator;
	}

	public String getQuoteChar() {
		return quoteChar;
	}

	public void setQuoteChar(String quoteChar) {
		this.quoteChar = quoteChar;
	}

	public abstract List<List<String>> getFileContent(File inputFile);

	public abstract List<List<String>> getFileContent(File inputFile, String cellSeparator, String rowSeparator, String quoteChar);

	public static String nullToEmptyString(String input) {
		if (input == null) {
			return "";
		}
		return input;
	}
}