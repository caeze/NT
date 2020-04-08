package filehandling;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

import console.Log;

/**
 * Excel file handling util.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class XlsFileHandlingUtil extends StructuredDataFileHandlingUtil {

	public List<List<String>> getFileContent(File inputFile) {
		return getFileContent(inputFile, null, null, null);
	}

	public List<List<String>> getFileContent(File inputFile, String cellSeparator, String rowSeparator, String quoteChar) {
		if (nullToEmptyString(cellSeparator).isEmpty()) {
			cellSeparator = ",";
		}
		if (nullToEmptyString(rowSeparator).isEmpty()) {
			rowSeparator = "\n";
		}
		if (nullToEmptyString(quoteChar).isEmpty()) {
			quoteChar = "\"";
		}
		this.setCellSeparator(cellSeparator);
		this.setRowSeparator(rowSeparator);
		this.setQuoteChar(quoteChar);
		
		List<List<String>> content = new ArrayList<>();
		try {
			DataFormatter formatter = new DataFormatter();
			FileInputStream file = new FileInputStream(inputFile);
			HSSFWorkbook workbook = new HSSFWorkbook(file);
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				HSSFSheet sheet = workbook.getSheetAt(i);
				Iterator<Row> rowIterator = sheet.iterator();
				List<List<String>> sheetContent = new ArrayList<>();
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					Iterator<Cell> cellIterator = row.cellIterator();
					List<String> rowContent = new ArrayList<>();
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						String cellContent = formatter.formatCellValue(cell);
						rowContent.add(cellContent);
					}
					sheetContent.add(rowContent);
				}
				content.addAll(sheetContent);
			}
			workbook.close();
		} catch (Exception e) {
			Log.error(XlsFileHandlingUtil.class, "Error on reading xls file: " + e.getMessage());
		}
		return content;
	}
}