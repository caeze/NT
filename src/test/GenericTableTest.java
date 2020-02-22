package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import view.util.GenericTable;

/**
 * Test class for {@link view.util.GenericTable}.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class GenericTableTest implements Testable {

	public boolean genericTableTest() {
		// prepare data
		SimpleData sd1 = new SimpleData(1, "TEST_1!");
		SimpleData sd2 = new SimpleData(2, "TEST_2!");
		List<SimpleData> items = new ArrayList<>();
		items.add(sd1);
		items.add(sd2);
		List<String> columnNames = Arrays.asList("INTEGER_NAME", "STRING_NAME");
		List<Class<?>> columnClasses = Arrays.asList(Integer.class, String.class);
		List<String> columnGetters = Arrays.asList("getA", "getB");
		List<String> columnSetters = Arrays.asList("setA", "setB");
		List<Integer> editableRows = Arrays.asList(1);

		// execute tests
		GenericTable<SimpleData> gt = new GenericTable<>(items, columnNames, columnClasses, columnGetters, columnSetters, editableRows);

		// return result
		boolean testResult = gt.getTable().getValueAt(0, 0).equals(new Integer(1));
		testResult &= gt.getTable().getValueAt(0, 1).equals("TEST_1!");
		testResult &= gt.getTable().getValueAt(1, 0).equals(new Integer(2));
		testResult &= gt.getTable().getValueAt(1, 1).equals("TEST_2!");

		gt.getTable().setValueAt("TEST_3!", 1, 1);
		testResult &= gt.getTable().getValueAt(1, 1).equals("TEST_3!");

		return testResult;
	}
}
