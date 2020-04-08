package test;

import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.util.UUID;

import model.Course;
import model.LazyAObject;
import model.NormalGrade;
import model.Student;

/**
 * Test class for {@link model.NormalGrade}.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class NormalGradeTest implements Testable {

	public boolean jsonTest() {
		// prepare data
		NormalGrade normalGrade = new NormalGrade(UUID.randomUUID(), new LazyAObject<Course>(UUID.randomUUID()), new LazyAObject<Student>(UUID.randomUUID()), LocalDate.now(), 3.7, "name", "comment");

		// execute tests
		String normalGradeData = normalGrade.toJsonString();
		NormalGrade normalGrade2 = (NormalGrade) new NormalGrade().fillFromJsonString(normalGradeData);

		// return result
		return normalGrade.equals(normalGrade2);
	}

	public boolean constructorsTest() {
		// execute tests
		boolean defaultConstructorFound = false;
		boolean copyConstructorFound = false;
		for (Constructor<?> c : NormalGrade.class.getDeclaredConstructors()) {
			defaultConstructorFound |= c.toString().equals("public " + NormalGrade.class.getName() + "()");
			copyConstructorFound |= c.toString().equals("public " + NormalGrade.class.getName() + "(" + NormalGrade.class.getName() + ")");
		}

		// return result
		return defaultConstructorFound & copyConstructorFound;
	}
}
