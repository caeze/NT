package view.util;

import java.text.DecimalFormat;

/**
 * Floating point utility class.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class FloatingPointUtil {

	public static final double FLOATING_POINT_DELTA = 0.00001;

	public static boolean isWithinMargin(double val, double targetVal, double margin) {
		return Math.abs(targetVal - val) <= margin;
	}

	public static boolean isInBetween(double valToTest, double val1, double val2) {
		if (val1 > val2) {
			return isInBetween(valToTest, val2, val1);
		}
		return (val1 - FLOATING_POINT_DELTA <= valToTest) && (valToTest <= val2 + FLOATING_POINT_DELTA);
	}

	public static String f2n(double value) {
		return f2n(value, 2);
	}

	public static String f2n(double value, int decimals) {
		if (decimals == 0) {
			double roundedVal = Math.round(value * 100.0) / 100.0;
			DecimalFormat f = new DecimalFormat("0");
			return f.format(roundedVal);
		}
		String decimalPlaces = "";
		for (int i = 0; i < decimals; i++) {
			decimalPlaces += "0";
		}
		double roundedVal = Math.round(value * 100.0) / 100.0;
		DecimalFormat f = new DecimalFormat("0." + decimalPlaces);
		return f.format(roundedVal);
	}
}
