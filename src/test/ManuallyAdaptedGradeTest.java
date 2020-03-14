package test;

import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.UUID;

import model.Course;
import model.LazyAObject;
import model.ManuallyAdaptedGrade;
import model.Student;

/**
 * Test class for {@link model.ManuallyAdaptedGrade}.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class ManuallyAdaptedGradeTest implements Testable {

	public boolean jsonTest() {
		// prepare data
		ManuallyAdaptedGrade manuallyAdaptedGrade = new ManuallyAdaptedGrade(UUID.randomUUID(), new LazyAObject<Course>(UUID.randomUUID()), new LazyAObject<Student>(UUID.randomUUID()), new Date(), 3.7, "name", "comment");

		// execute tests
		String manuallyAdaptedGradeData = manuallyAdaptedGrade.toJsonString();
		ManuallyAdaptedGrade manuallyAdaptedGrade2 = (ManuallyAdaptedGrade) new ManuallyAdaptedGrade().fillFromJsonString(manuallyAdaptedGradeData);

		// return result
		return manuallyAdaptedGrade.equals(manuallyAdaptedGrade2);
	}

	public boolean constructorsTest() {
		// execute tests
		boolean defaultConstructorFound = false;
		boolean copyConstructorFound = false;
		for (Constructor<?> c : ManuallyAdaptedGrade.class.getDeclaredConstructors()) {
			defaultConstructorFound |= c.toString().equals("public " + ManuallyAdaptedGrade.class.getName() + "()");
			copyConstructorFound |= c.toString().equals("public " + ManuallyAdaptedGrade.class.getName() + "(" + ManuallyAdaptedGrade.class.getName() + ")");
		}

		// return result
		return defaultConstructorFound & copyConstructorFound;
	}
}
