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

	public ComputedAverageGrade() {
	}

	public ComputedAverageGrade(UUID uuid, LazyAObject<Course> course, LazyAObject<Student> student, Date date, double grade, String name, String comment) {
		super(uuid, course, student, date, grade, name, comment);
	}

	public ComputedAverageGrade(ComputedAverageGrade other) {
		super(other);
	}
}
