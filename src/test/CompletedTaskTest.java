package test;

import java.lang.reflect.Constructor;
import java.util.UUID;

import model.CompletedTask;
import model.LazyAObject;
import model.Student;
import model.Task;

/**
 * Test class for {@link model.CompletedTask}.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class CompletedTaskTest implements Testable {

	public boolean jsonTest() {
		// prepare data
		CompletedTask completedTask = new CompletedTask(UUID.randomUUID(), new LazyAObject<Task>(UUID.randomUUID()), new LazyAObject<Student>(UUID.randomUUID()), 4.3, "comment");

		// execute tests
		String completedTaskData = completedTask.toJsonString();
		CompletedTask completedTask2 = (CompletedTask) new CompletedTask().fillFromJsonString(completedTaskData);

		// return result
		return completedTask.equals(completedTask2);
	}

	public boolean constructorsTest() {
		// execute tests
		boolean defaultConstructorFound = false;
		boolean copyConstructorFound = false;
		for (Constructor<?> c : CompletedTask.class.getDeclaredConstructors()) {
			defaultConstructorFound |= c.toString().equals("public " + CompletedTask.class.getName() + "()");
			copyConstructorFound |= c.toString().equals("public " + CompletedTask.class.getName() + "(" + CompletedTask.class.getName() + ")");
		}

		// return result
		return defaultConstructorFound & copyConstructorFound;
	}
}
