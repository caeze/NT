package model;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Data class for a manually adapted grade.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class ManuallyAdaptedGrade extends AGrade {

	public ManuallyAdaptedGrade() {
	}

	public ManuallyAdaptedGrade(UUID uuid, LazyAObject<Course> course, LazyAObject<Student> student, LocalDate date, double grade, String name, String comment) {
		super(uuid, course, student, date, grade, name, comment);
	}

	public ManuallyAdaptedGrade(ManuallyAdaptedGrade other) {
		super(other);
	}
}
