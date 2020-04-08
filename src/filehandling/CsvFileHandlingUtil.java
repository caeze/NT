package filehandling;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import console.Log;

/**
 * CSV file handling util.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class CsvFileHandlingUtil extends StructuredDataFileHandlingUtil {

	public List<List<String>> getFileContent(File inputFile) {
		return getFileContent(inputFile, null, null, null);
	}

	public List<List<String>> getFileContent(File inputFile, String cellSeparator, String rowSeparator, String quoteChar) {
		String fileContent = FileHandlingUtil.getInstance().readFileAsString(inputFile.getAbsolutePath());
		return getFileContent(fileContent, cellSeparator, rowSeparator, quoteChar);
	}

	public List<List<String>> getFileContent(String fileContent) {
		return getFileContent(fileContent, null, null, null);
	}

	public List<List<String>> getFileContent(String fileContent, String cellSeparator, String rowSeparator) {
		return getFileContent(fileContent, cellSeparator, rowSeparator, null);
	}

	public List<List<String>> getFileContent(String fileContent, String cellSeparator, String rowSeparator, String quoteChar) {
		if (nullToEmptyString(cellSeparator).isEmpty()) {
			cellSeparator = determineCellSeparator(fileContent) + "";
		}
		if (nullToEmptyString(rowSeparator).isEmpty()) {
			rowSeparator = determineRowSeparator(fileContent);
		}
		if (nullToEmptyString(quoteChar).isEmpty()) {
			quoteChar = determineQuoteChar(fileContent) + "";
		}
		this.setCellSeparator(cellSeparator);
		this.setRowSeparator(rowSeparator);
		this.setQuoteChar(quoteChar);

		List<List<String>> content = new ArrayList<>();
		try {
			Iterable<CSVRecord> records = CSVParser.parse(fileContent, CSVFormat.DEFAULT.withDelimiter(cellSeparator.charAt(0)).withRecordSeparator(rowSeparator).withQuote(quoteChar.charAt(0)));
			for (CSVRecord record : records) {
				List<String> rowContent = new ArrayList<>();
				for (int i = 0; i < record.size(); i++) {
					String cellContent = nullToEmptyString(record.get(i));
					rowContent.add(cellContent);
				}
				content.add(rowContent);
			}
		} catch (Exception e) {
			Log.error(CsvFileHandlingUtil.class, "Error on reading CSV file: " + e.getMessage());
		}
		return content;
	}

	private char determineCellSeparator(String fileContent) {
		// tone down the occurrences of dots a bit
		return findMaxOccurringString(fileContent, Arrays.asList(",", ";", "\t", " ", "."), Arrays.asList(1.0, 1.0, 1.0, 1.0, 0.5)).charAt(0);
	}

	private String determineRowSeparator(String fileContent) {
		return findMaxOccurringString(fileContent, Arrays.asList("\r\n", "\n", "\n\r", "\r"), Arrays.asList(1.0, 1.0, 1.0, 1.0));
	}

	private char determineQuoteChar(String fileContent) {
		int noOfDoubleQuotes = countOccurrences(fileContent, "\"");
		int noOfSingleQuotes = countOccurrences(fileContent, "'");
		if (noOfDoubleQuotes * 1.5 < noOfSingleQuotes) {
			return '\'';
		}
		return '"';
	}

	private String findMaxOccurringString(String fileContent, List<String> charsToCheck, List<Double> weights) {
		Map<String, Integer> numberOccurrencesToSeparators = new HashMap<>();
		for (int i = 0; i < charsToCheck.size(); i++) {
			String c = charsToCheck.get(i);
			int o = countOccurrences(fileContent, c);
			numberOccurrencesToSeparators.put(c, (int) (weights.get(i) * o));
		}

		List<Integer> occurrenceCounts = new ArrayList<>(numberOccurrencesToSeparators.values());
		Collections.sort(occurrenceCounts);
		Collections.sort(occurrenceCounts, Collections.reverseOrder());

		for (Integer i : occurrenceCounts) {
			for (String c : charsToCheck) {
				if (numberOccurrencesToSeparators.get(c).equals(i)) {
					return c;
				}
			}
		}
		return ",";
	}

	private int countOccurrences(String s, String toReplace) {
		return s.length() - s.replace(toReplace, "").length();
	}
}