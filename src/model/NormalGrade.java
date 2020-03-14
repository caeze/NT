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

	public NormalGrade() {
	}

	public NormalGrade(UUID uuid, LazyAObject<Course> course, LazyAObject<Student> student, Date date, double grade, String name, String comment) {
		super(uuid, course, student, date, grade, name, comment);
	}

	public NormalGrade(NormalGrade other) {
		super(other);
	}
}
