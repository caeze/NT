package test;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.UUID;

import model.Course;
import model.MissingAObject;
import model.Model;
import model.Project;
import model.Room;
import model.Student;
import nt.NT;
import view.img.ImageStore;

/**
 * Test class for {@link model.Project}.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class ProjectTest implements Testable {

	public boolean jsonTest() {
		// prepare data
		Model.getInstance().loadEmptyProject();
		Project project = Model.getInstance().getCurrentProject();

		byte[] bytes = ImageStore.getBytesFromImage(ImageStore.getScaledImage(ImageStore.getImageIcon(""), NT.STUDENT_IMAGE_WIDTH, NT.STUDENT_IMAGE_HEIGHT));
		Student student = new Student(UUID.randomUUID(), "firstName", "lastName", LocalDate.now(), "email", "mobilePhone", "comment", bytes);
		Room room = new Room(UUID.randomUUID(), "", new ArrayList<>(), "");
		Course course = new Course(UUID.randomUUID(), "", "", "", new MissingAObject<Room>(), new ArrayList<>(), new ArrayList<>(), "");

		project.getStudents().add(student);
		project.getRooms().add(room);
		project.getCourses().add(course);

		// execute tests
		String projectData = project.toJsonString();
		Project project2 = (Project) new Project().fillFromJsonString(projectData);

		// return result
		return project.equals(project2);
	}

	public boolean constructorsTest() {
		// execute tests
		boolean defaultConstructorFound = false;
		boolean copyConstructorFound = false;
		for (Constructor<?> c : Project.class.getDeclaredConstructors()) {
			defaultConstructorFound |= c.toString().equals("public " + Project.class.getName() + "()");
			copyConstructorFound |= c.toString().equals("public " + Project.class.getName() + "(" + Project.class.getName() + ")");
		}

		// return result
		return defaultConstructorFound & copyConstructorFound;
	}
}
