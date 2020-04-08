package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;

/**
 * Class for handling different date formats.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class DateUtil {

	private static DateUtil instance;

	private static String[] SEPARATORS = { ".", " ", "/", "-" };
	private static String[] POSSIBLE_DATE_FORMATS = { "dd.MM.yyyy", "dd.MM.yy", "dd.MMM.yyyy", "dd.MMM.yy", "yyyy.MM.dd", "yy.MM.dd", "yyyy.MMM.dd", "yy.MMM.dd", "MM.dd.yy", "MM.dd.yyyy", "MMM.dd.yy", "MMM.dd.yyyy" };

	private DateTimeFormatter dateTimeFormatter;
	private DateTimeFormatter dateTimeFormatterFullYearFormat;

	private DateUtil() {
		// hide constructor, singleton pattern
	}

	/**
	 * Get an instance, singleton pattern.
	 *
	 * @return an instance
	 */
	public static DateUtil getInstance() {
		if (instance == null) {
			instance = new DateUtil();
			String dateFormats = "";
			String dateFormatsFullYearFormat = "";
			for (String f : POSSIBLE_DATE_FORMATS) {
				for (String s : SEPARATORS) {
					if (f.contains("yyyy")) {
						dateFormatsFullYearFormat += "[" + f.replace(".", s) + "]";
					} else {
						dateFormats += "[" + f.replace(".", s) + "]";
					}
				}
			}
			DateTimeFormatterBuilder dateTimeFormatterBuilder = new DateTimeFormatterBuilder().append(DateTimeFormatter.ofPattern(dateFormats));
			instance.dateTimeFormatter = dateTimeFormatterBuilder.toFormatter();

			DateTimeFormatterBuilder dateTimeFormatterBuilderFullYearFormat = new DateTimeFormatterBuilder().append(DateTimeFormatter.ofPattern(dateFormatsFullYearFormat));
			instance.dateTimeFormatterFullYearFormat = dateTimeFormatterBuilderFullYearFormat.toFormatter();
		}
		return instance;
	}

	/*
	 * , "yyyy.MM.dd G 'at' HH:mm:ss z", "EEE, MMM d, ''yy", "h:mm a",
	 * "hh 'o''clock' a, zzzz", "K:mm a, z", "yyyyy.MMMMM.dd GGG hh:mm aaa",
	 * "EEE, d MMM yyyy HH:mm:ss Z", "yyMMddHHmmssZ", "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
	 * "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", "YYYY-'W'ww-u",
	 * "EEE, dd MMM yyyy HH:mm:ss z", "EEE, dd MMM yyyy HH:mm zzzz",
	 * "yyyy-MM-dd'T'HH:mm:ssZ", "yyyy-MM-dd'T'HH:mm:ss.SSSzzzz",
	 * "yyyy-MM-dd'T'HH:mm:sszzzz", "yyyy-MM-dd'T'HH:mm:ss z",
	 * "yyyy-MM-dd'T'HH:mm:ssz", "yyyy-MM-dd'T'HH:mm:ss",
	 * "yyyy-MM-dd'T'HHmmss.SSSz", "yyyy-MM-dd", "yyyyMMdd", "dd/MM/yy",
	 * "dd/MM/yyyy", "MM/dd/yy", "MM/dd/yyyy", "dd-MM-yy", "dd-MM-yyyy", "MM-dd-yy",
	 * "MM-dd-yyyy"
	 */

	public LocalDate getDate(String inputString) {
		try {
			LocalDate ld = LocalDate.parse(inputString, dateTimeFormatter);
			LocalDate now = LocalDate.now();
			if (now.plusYears(30).isBefore(ld)) {
				ld = ld.minusYears(100);
			}
			return ld;
		} catch (DateTimeParseException e) {
			// if this can not be parsed, this is not a problem
		}
		return getDateFullYearFormat(inputString);
	}

	private LocalDate getDateFullYearFormat(String inputString) {
		try {
			return LocalDate.parse(inputString, dateTimeFormatterFullYearFormat);
		} catch (DateTimeParseException e) {
			// if this can not be parsed, this is not a problem
		}
		return null;
	}
}
