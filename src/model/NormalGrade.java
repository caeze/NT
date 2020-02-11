package model;

import java.util.Date;
import java.util.UUID;

/**
 * Data class for a normal grade.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class NormalGrade extends AGrade {

	public NormalGrade(UUID uuid, Course course, Student student, Date date, double grade, String name, String comment) {
		super(uuid, course, student, date, grade, name, comment);
	}

	public static String toJsonString(NormalGrade grade) {
		return AGrade.toJsonString(grade);
	}

	public static NormalGrade fromJsonString(String jsonString) {
		return (NormalGrade) AGrade.fromJsonString(jsonString);
	}
}
