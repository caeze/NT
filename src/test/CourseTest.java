package test;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.UUID;

import model.Course;
import model.MissingAObject;
import model.Model;
import model.Room;

/**
 * Test class for {@link model.Course}.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class CourseTest implements Testable {

	public boolean jsonTest() {
		// prepare data
		Model.getInstance().loadEmptyProject();
		Course course = new Course(UUID.randomUUID(), "", "", "", new MissingAObject<Room>(), new ArrayList<>(), new ArrayList<>(), "");

		// execute tests
		String courseData = course.toJsonString();
		Course course2 = (Course) new Course().fillFromJsonString(courseData);

		// return result
		return course.equals(course2);
	}

	public boolean constructorsTest() {
		// execute tests
		boolean defaultConstructorFound = false;
		boolean copyConstructorFound = false;
		for (Constructor<?> c : Course.class.getDeclaredConstructors()) {
			defaultConstructorFound |= c.toString().equals("public " + Course.class.getName() + "()");
			copyConstructorFound |= c.toString().equals("public " + Course.class.getName() + "(" + Course.class.getName() + ")");
		}

		// return result
		return defaultConstructorFound & copyConstructorFound;
	}
}
