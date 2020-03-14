package test;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.UUID;

import model.CharacteristicCurve;
import model.RelativePoint;

/**
 * Test class for {@link model.CharacteristicCurve}.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class CharacteristicCurveTest implements Testable {

	public boolean jsonTest() {
		// prepare data
		CharacteristicCurve characteristicCurve = new CharacteristicCurve(UUID.randomUUID(), Arrays.asList(new RelativePoint(UUID.randomUUID(), 1, 2), new RelativePoint(UUID.randomUUID(), 3, 4)), "comment");

		// execute tests
		String characteristicCurveData = characteristicCurve.toJsonString();
		CharacteristicCurve characteristicCurve2 = (CharacteristicCurve) new CharacteristicCurve().fillFromJsonString(characteristicCurveData);

		// return result
		return characteristicCurve.equals(characteristicCurve2);
	}

	public boolean constructorsTest() {
		// execute tests
		boolean defaultConstructorFound = false;
		boolean copyConstructorFound = false;
		for (Constructor<?> c : CharacteristicCurve.class.getDeclaredConstructors()) {
			defaultConstructorFound |= c.toString().equals("public " + CharacteristicCurve.class.getName() + "()");
			copyConstructorFound |= c.toString().equals("public " + CharacteristicCurve.class.getName() + "(" + CharacteristicCurve.class.getName() + ")");
		}

		// return result
		return defaultConstructorFound & copyConstructorFound;
	}
}
