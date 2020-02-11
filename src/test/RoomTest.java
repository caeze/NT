package test;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import model.RelativePoint;
import model.Room;
import model.Student;
import model.Table;

/**
 * Test class for {@link model.Room}.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class RoomTest implements Testable {

	public boolean roomTest() {
		// prepare data
		byte[] bytes = new byte[100];
		for (int i = 0; i < bytes.length; i++) {
			int index = (int) i / 3;
			bytes[i] = (byte) index;
		}
		Student student1 = new Student(UUID.randomUUID(), "firstName", "lastName", new Date(), "email", "mobilePhone", "comment", bytes);
		Student student2 = new Student(UUID.randomUUID(), "firstName", "lastName", new Date(), "email", "mobilePhone", "comment", bytes);
		Table table1 = new Table(UUID.randomUUID(), new RelativePoint(1, 2), Arrays.asList(student1, student2), "comment");
		Table table2 = new Table(UUID.randomUUID(), new RelativePoint(3, 4), Arrays.asList(student1, student2), "comment");
		Room room = new Room(UUID.randomUUID(), "name", Arrays.asList(table1, table2), "comment");
		
		// execute tests
		String roomData = Room.toJsonString(room);
		Room room2 = Room.fromJsonString(roomData);

		// return result
		return room.equals(room2);
	}
}
