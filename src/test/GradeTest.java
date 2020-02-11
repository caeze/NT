package test;

import java.util.Date;
import java.util.UUID;

import model.ComputedAverageGrade;
import model.Course;
import model.ManuallyAdaptedGrade;
import model.NormalGrade;
import model.Student;

/**
 * Test class for {@link model.Grade}.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class GradeTest implements Testable {

	public boolean normalGradeTest() {
		// prepare data
		Course course = new Course(UUID.randomUUID(), "subject", "term", "grade", "letter", "comment");
		byte[] bytes = new byte[100];
		for (int i = 0; i < bytes.length; i++) {
			int index = (int) i / 3;
			bytes[i] = (byte) index;
		}
		Student student = new Student(UUID.randomUUID(), "firstName", "lastName", new Date(), "email", "mobilePhone", "comment", bytes);
		NormalGrade grade = new NormalGrade(UUID.randomUUID(), course, student, new Date(), 1.5, "name", "comment");

		// execute tests
		String gradeData = NormalGrade.toJsonString(grade);
		NormalGrade grade2 = NormalGrade.fromJsonString(gradeData);

		// return result
		return grade.equals(grade2);
	}

	public boolean manuallyAdaptedGradeTest() {
		// prepare data
		Course course = new Course(UUID.randomUUID(), "subject", "term", "grade", "letter", "comment");
		byte[] bytes = new byte[100];
		for (int i = 0; i < bytes.length; i++) {
			int index = (int) i / 3;
			bytes[i] = (byte) index;
		}
		Student student = new Student(UUID.randomUUID(), "firstName", "lastName", new Date(), "email", "mobilePhone", "comment", bytes);
		ManuallyAdaptedGrade grade = new ManuallyAdaptedGrade(UUID.randomUUID(), course, student, new Date(), 1.5, "name", "comment");

		// execute tests
		String gradeData = ManuallyAdaptedGrade.toJsonString(grade);
		ManuallyAdaptedGrade grade2 = ManuallyAdaptedGrade.fromJsonString(gradeData);

		// return result
		return grade.equals(grade2);
	}

	public boolean computedAverageGradeTest() {
		// prepare data
		Course course = new Course(UUID.randomUUID(), "subject", "term", "grade", "letter", "comment");
		byte[] bytes = new byte[100];
		for (int i = 0; i < bytes.length; i++) {
			int index = (int) i / 3;
			bytes[i] = (byte) index;
		}
		Student student = new Student(UUID.randomUUID(), "firstName", "lastName", new Date(), "email", "mobilePhone", "comment", bytes);
		ComputedAverageGrade grade = new ComputedAverageGrade(UUID.randomUUID(), course, student, new Date(), 1.5, "name", "comment");

		// execute tests
		String gradeData = ComputedAverageGrade.toJsonString(grade);
		ComputedAverageGrade grade2 = ComputedAverageGrade.fromJsonString(gradeData);

		// return result
		return grade.equals(grade2);
	}
}
