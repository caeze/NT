package test;

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

	public boolean caracteristicCurveTest() {
		// prepare data
		CharacteristicCurve characteristicCurve = new CharacteristicCurve(UUID.randomUUID(), Arrays.asList(new RelativePoint(1, 2), new RelativePoint(3, 4)), "comment");
		
		// execute tests
		String characteristicCurveData = CharacteristicCurve.toJsonString(characteristicCurve);
		CharacteristicCurve characteristicCurve2 = CharacteristicCurve.fromJsonString(characteristicCurveData);

		// return result
		return characteristicCurve.equals(characteristicCurve2);
	}
}
