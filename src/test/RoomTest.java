package test;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.UUID;

import model.Model;
import model.Room;

/**
 * Test class for {@link model.Room}.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class RoomTest implements Testable {

	public boolean jsonTest() {
		// prepare data
		Model.getInstance().loadEmptyProject();
		Room room = new Room(UUID.randomUUID(), "", new ArrayList<>(), "");

		// execute tests
		String roomData = room.toJsonString();
		Room room2 = (Room) new Room().fillFromJsonString(roomData);

		// return result
		return room.equals(room2);
	}

	public boolean constructorsTest() {
		// execute tests
		boolean defaultConstructorFound = false;
		boolean copyConstructorFound = false;
		for (Constructor<?> c : Room.class.getDeclaredConstructors()) {
			defaultConstructorFound |= c.toString().equals("public " + Room.class.getName() + "()");
			copyConstructorFound |= c.toString().equals("public " + Room.class.getName() + "(" + Room.class.getName() + ")");
		}

		// return result
		return defaultConstructorFound & copyConstructorFound;
	}
}
