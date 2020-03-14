package test;

import java.lang.reflect.Constructor;
import java.util.UUID;

import model.RelativePoint;

/**
 * Test class for {@link model.RelativePoint}.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class RelativePointTest implements Testable {

	public boolean jsonTest() {
		// prepare data
		RelativePoint relativePoint = new RelativePoint(UUID.randomUUID(), 1.2, 3.4);

		// execute tests
		String relativePointData = relativePoint.toJsonString();
		RelativePoint relativePoint2 = (RelativePoint) new RelativePoint().fillFromJsonString(relativePointData);

		// return result
		return relativePoint.equals(relativePoint2);
	}

	public boolean constructorsTest() {
		// execute tests
		boolean defaultConstructorFound = false;
		boolean copyConstructorFound = false;
		for (Constructor<?> c : RelativePoint.class.getDeclaredConstructors()) {
			defaultConstructorFound |= c.toString().equals("public " + RelativePoint.class.getName() + "()");
			copyConstructorFound |= c.toString().equals("public " + RelativePoint.class.getName() + "(" + RelativePoint.class.getName() + ")");
		}

		// return result
		return defaultConstructorFound & copyConstructorFound;
	}
}
