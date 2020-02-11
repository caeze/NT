package test;

import java.util.Date;
import java.util.UUID;

import model.Student;

/**
 * Test class for {@link model.Student}.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class StudentTest implements Testable {

	public boolean studentTest() {
		// prepare data
		byte[] bytes = new byte[100];
		for (int i = 0; i < bytes.length; i++) {
			int index = (int) i / 3;
			bytes[i] = (byte) index;
		}
		Student student = new Student(UUID.randomUUID(), "firstName", "lastName", new Date(), "email", "mobilePhone", "comment", bytes);

		// execute tests
		String studentData = Student.toJsonString(student);
		Student student2 = Student.fromJsonString(studentData);

		// return result
		return student.equals(student2);
	}
}
