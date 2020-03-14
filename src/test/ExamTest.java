package test;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import model.CharacteristicCurve;
import model.CompletedTask;
import model.Exam;
import model.GradeType;
import model.MissingAObject;
import model.RelativePoint;
import model.Student;
import model.Task;

/**
 * Test class for {@link model.Exam}.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class ExamTest implements Testable {

	public boolean jsonTest() {
		// prepare data
		List<Task> tasks = new ArrayList<>();
		tasks.add(new Task(UUID.randomUUID(), "name", 2.345, "comment"));
		List<CompletedTask> completedTasks = new ArrayList<>();
		completedTasks.add(new CompletedTask(UUID.randomUUID(), new MissingAObject<Task>(), new MissingAObject<Student>(), 1.54367, "comment"));
		CharacteristicCurve characteristicCurve = new CharacteristicCurve(UUID.randomUUID(), new ArrayList<RelativePoint>(), "comment");
		Exam exam = new Exam(UUID.randomUUID(), "name", GradeType.DefaultGradeTypes.GRADES_1_TO_5, characteristicCurve, tasks, completedTasks, "comment");

		// execute tests
		String examData = exam.toJsonString();
		Exam exam2 = (Exam) new Exam().fillFromJsonString(examData);

		// return result
		return exam.equals(exam2);
	}

	public boolean constructorsTest() {
		// execute tests
		boolean defaultConstructorFound = false;
		boolean copyConstructorFound = false;
		for (Constructor<?> c : Exam.class.getDeclaredConstructors()) {
			defaultConstructorFound |= c.toString().equals("public " + Exam.class.getName() + "()");
			copyConstructorFound |= c.toString().equals("public " + Exam.class.getName() + "(" + Exam.class.getName() + ")");
		}

		// return result
		return defaultConstructorFound & copyConstructorFound;
	}
}
