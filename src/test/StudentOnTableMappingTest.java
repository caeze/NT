package test;

import java.lang.reflect.Constructor;
import java.util.UUID;

import model.StudentOnTableMapping;

/**
 * Test class for {@link model.StudentOnTableMapping}.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class StudentOnTableMappingTest implements Testable {

	public boolean jsonTest() {
		// prepare data
		StudentOnTableMapping sotm = new StudentOnTableMapping(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());

		// execute tests
		String sotmData = sotm.toJsonString();
		StudentOnTableMapping sotm2 = (StudentOnTableMapping) new StudentOnTableMapping().fillFromJsonString(sotmData);

		// return result
		return sotm.equals(sotm2);
	}

	public boolean constructorsTest() {
		// execute tests
		boolean defaultConstructorFound = false;
		boolean copyConstructorFound = false;
		for (Constructor<?> c : StudentOnTableMapping.class.getDeclaredConstructors()) {
			defaultConstructorFound |= c.toString().equals("public " + StudentOnTableMapping.class.getName() + "()");
			copyConstructorFound |= c.toString().equals("public " + StudentOnTableMapping.class.getName() + "(" + StudentOnTableMapping.class.getName() + ")");
		}

		// return result
		return defaultConstructorFound & copyConstructorFound;
	}
}
