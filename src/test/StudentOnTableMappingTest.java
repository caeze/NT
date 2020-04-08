package test;

import java.lang.reflect.Constructor;
import java.util.UUID;

import model.LazyAObject;
import model.Student;
import model.StudentOnTableMapping;
import model.Table;

/**
 * Test class for {@link model.StudentOnTableMapping}.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class StudentOnTableMappingTest implements Testable {

	public boolean jsonTest() {
		// prepare data
		StudentOnTableMapping sotm = new StudentOnTableMapping(UUID.randomUUID(), new LazyAObject<Student>(UUID.randomUUID()), new LazyAObject<Table>(UUID.randomUUID()), 2);

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
