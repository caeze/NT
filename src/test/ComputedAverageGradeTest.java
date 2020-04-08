package test;

import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.util.UUID;

import model.ComputedAverageGrade;
import model.Course;
import model.LazyAObject;
import model.Student;

/**
 * Test class for {@link model.ComputedAverageGrade}.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class ComputedAverageGradeTest implements Testable {

	public boolean jsonTest() {
		// prepare data
		ComputedAverageGrade computedAverageGrade = new ComputedAverageGrade(UUID.randomUUID(), new LazyAObject<Course>(UUID.randomUUID()), new LazyAObject<Student>(UUID.randomUUID()), LocalDate.now(), 3.7, "name", "comment");

		// execute tests
		String computedAverageGradeData = computedAverageGrade.toJsonString();
		ComputedAverageGrade computedAverageGrade2 = (ComputedAverageGrade) new ComputedAverageGrade().fillFromJsonString(computedAverageGradeData);

		// return result
		return computedAverageGrade.equals(computedAverageGrade2);
	}

	public boolean constructorsTest() {
		// execute tests
		boolean defaultConstructorFound = false;
		boolean copyConstructorFound = false;
		for (Constructor<?> c : ComputedAverageGrade.class.getDeclaredConstructors()) {
			defaultConstructorFound |= c.toString().equals("public " + ComputedAverageGrade.class.getName() + "()");
			copyConstructorFound |= c.toString().equals("public " + ComputedAverageGrade.class.getName() + "(" + ComputedAverageGrade.class.getName() + ")");
		}

		// return result
		return defaultConstructorFound & copyConstructorFound;
	}
}
