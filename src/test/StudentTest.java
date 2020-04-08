package test;

import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.util.UUID;

import model.Model;
import model.Student;
import nt.NT;
import view.img.ImageStore;

/**
 * Test class for {@link model.Student}.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class StudentTest implements Testable {

	public boolean jsonTest() {
		// prepare data
		Model.getInstance().loadEmptyProject();
		byte[] bytes = ImageStore.getBytesFromImage(ImageStore.getScaledImage(ImageStore.getImageIcon(""), NT.STUDENT_IMAGE_WIDTH, NT.STUDENT_IMAGE_HEIGHT));
		Student student = new Student(UUID.randomUUID(), "firstName", "lastName", LocalDate.now(), "email", "mobilePhone", "comment", bytes);

		// execute tests
		String studentData = student.toJsonString();
		Student student2 = (Student) new Student().fillFromJsonString(studentData);

		// return result
		return student.equals(student2);
	}

	public boolean constructorsTest() {
		// execute tests
		boolean defaultConstructorFound = false;
		boolean copyConstructorFound = false;
		for (Constructor<?> c : Student.class.getDeclaredConstructors()) {
			defaultConstructorFound |= c.toString().equals("public " + Student.class.getName() + "()");
			copyConstructorFound |= c.toString().equals("public " + Student.class.getName() + "(" + Student.class.getName() + ")");
		}

		// return result
		return defaultConstructorFound & copyConstructorFound;
	}
}
