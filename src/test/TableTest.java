package test;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import model.RelativePoint;
import model.Student;
import model.Table;

/**
 * Test class for {@link model.Table}.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class TableTest implements Testable {

	public boolean tableTest() {
		// prepare data
		byte[] bytes = new byte[100];
		for (int i = 0; i < bytes.length; i++) {
			int index = (int) i / 3;
			bytes[i] = (byte) index;
		}
		Student student1 = new Student(UUID.randomUUID(), "firstName", "lastName", new Date(), "email", "mobilePhone", "comment", bytes);
		Student student2 = new Student(UUID.randomUUID(), "firstName", "lastName", new Date(), "email", "mobilePhone", "comment", bytes);
		Table table = new Table(UUID.randomUUID(), new RelativePoint(1, 2), Arrays.asList(student1, student2), "comment");

		// execute tests
		String tableData = Table.toJsonString(table);
		Table table2 = Table.fromJsonString(tableData);

		// return result
		return table.equals(table2);
	}
}
