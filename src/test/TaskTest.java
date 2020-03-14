package test;

import java.lang.reflect.Constructor;
import java.util.UUID;

import model.Model;
import model.Task;

/**
 * Test class for {@link model.Task}.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class TaskTest implements Testable {

	public boolean jsonTest() {
		// prepare data
		Model.getInstance().loadEmptyProject();
		Task task = new Task(UUID.randomUUID(), "name", 36.75, "comment");

		// execute tests
		String taskData = task.toJsonString();
		Task task2 = (Task) new Task().fillFromJsonString(taskData);

		// return result
		return task.equals(task2);
	}

	public boolean constructorsTest() {
		// execute tests
		boolean defaultConstructorFound = false;
		boolean copyConstructorFound = false;
		for (Constructor<?> c : Task.class.getDeclaredConstructors()) {
			defaultConstructorFound |= c.toString().equals("public " + Task.class.getName() + "()");
			copyConstructorFound |= c.toString().equals("public " + Task.class.getName() + "(" + Task.class.getName() + ")");
		}

		// return result
		return defaultConstructorFound & copyConstructorFound;
	}
}
