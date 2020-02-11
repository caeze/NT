package model;

import java.util.Date;
import java.util.UUID;

/**
 * Data class for a computed average grade.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class ComputedAverageGrade extends AGrade {

	public ComputedAverageGrade(UUID uuid, Course course, Student student, Date date, double grade, String name, String comment) {
		super(uuid, course, student, date, grade, name, comment);
	}

	public static String toJsonString(ComputedAverageGrade grade) {
		return AGrade.toJsonString(grade);
	}

	public static ComputedAverageGrade fromJsonString(String jsonString) {
		return (ComputedAverageGrade) AGrade.fromJsonString(jsonString);
	}
}
