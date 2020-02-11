package test;

import java.util.UUID;

import model.Course;

/**
 * Test class for {@link model.Course}.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class CourseTest implements Testable {

	public boolean courseTest() {
		// prepare data
		Course course = new Course(UUID.randomUUID(), "subject", "term", "grade", "letter", "comment");

		// execute tests
		String courseData = Course.toJsonString(course);
		Course course2 = Course.fromJsonString(courseData);

		// return result
		return course.equals(course2);
	}
}
