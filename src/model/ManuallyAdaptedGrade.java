package model;

import java.util.Date;
import java.util.UUID;

/**
 * Data class for a manually adapted grade.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class ManuallyAdaptedGrade extends AGrade {

	public ManuallyAdaptedGrade(UUID uuid, Course course, Student student, Date date, double grade, String name, String comment) {
		super(uuid, course, student, date, grade, name, comment);
	}

	public static String toJsonString(ManuallyAdaptedGrade grade) {
		return AGrade.toJsonString(grade);
	}

	public static ManuallyAdaptedGrade fromJsonString(String jsonString) {
		return (ManuallyAdaptedGrade) AGrade.fromJsonString(jsonString);
	}
}
