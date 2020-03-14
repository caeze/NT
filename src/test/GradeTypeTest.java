package test;

import java.lang.reflect.Constructor;

import model.GradeType;

/**
 * Test class for {@link model.GradeType}.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class GradeTypeTest implements Testable {

	public boolean jsonTest() {
		// prepare data
		GradeType gradeType = GradeType.DefaultGradeTypes.GRADES_1_TO_6;

		// execute tests
		String gradeTypeData = gradeType.toJsonString();
		GradeType gradeType2 = (GradeType) new GradeType().fillFromJsonString(gradeTypeData);

		// return result
		return gradeType.equals(gradeType2);
	}

	public boolean constructorsTest() {
		// execute tests
		boolean defaultConstructorFound = false;
		boolean copyConstructorFound = false;
		for (Constructor<?> c : GradeType.class.getDeclaredConstructors()) {
			defaultConstructorFound |= c.toString().equals("public " + GradeType.class.getName() + "()");
			copyConstructorFound |= c.toString().equals("public " + GradeType.class.getName() + "(" + GradeType.class.getName() + ")");
		}

		// return result
		return defaultConstructorFound & copyConstructorFound;
	}
}
