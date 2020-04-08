package model;

import java.time.LocalDate;
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

	public NormalGrade(UUID uuid, LazyAObject<Course> course, LazyAObject<Student> student, LocalDate date, double grade, String name, String comment) {
		super(uuid, course, student, date, grade, name, comment);
	}

	public NormalGrade(NormalGrade other) {
		super(other);
	}
}
