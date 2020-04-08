package test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import console.Log;
import util.DateUtil;

/**
 * Test class for {@link util.DateUtil}.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class DateUtilTest implements Testable {

	public boolean testValidDates() {
		try {
			// prepare data
			List<String> datesToTest = Arrays.asList("20.01.2030", "06/09/23", "06/09/1523", "Jan 05 78", "Feb-15-78", "05 Jan 7178", "15 Feb 99");
			DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			List<LocalDate> expectedResult = Arrays.asList(LocalDate.parse("20/01/2030", df), LocalDate.parse("06/09/2023", df), LocalDate.parse("06/09/1523", df), LocalDate.parse("05/01/1978", df), LocalDate.parse("15/02/1978", df), LocalDate.parse("05/01/7178", df), LocalDate.parse("15/02/1999", df));

			// execute tests
			boolean result = true;
			for (int i = 0; i < datesToTest.size(); i++) {
				LocalDate parsedDate = DateUtil.getInstance().getDate(datesToTest.get(i));
				result &= expectedResult.get(i).equals(parsedDate);
			}

			// return result
			return result;
		} catch (Exception e) {
			Log.error(DateUtilTest.class, "Exception on DateUtilTest: " + e.getMessage());
		}
		return false;
	}

	public boolean testInvalidDates() {
		// prepare data
		List<String> datesToTest = Arrays.asList("20..01.2030", "", "99/99/1233", "08.03/1899", "08-03.1899");

		// execute tests
		boolean result = true;
		for (int i = 0; i < datesToTest.size(); i++) {
			LocalDate parsedDate = DateUtil.getInstance().getDate(datesToTest.get(i));
			result &= parsedDate == null;
		}

		// return result
		return result;
	}
}
