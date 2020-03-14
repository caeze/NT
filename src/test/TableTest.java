package test;

import java.lang.reflect.Constructor;
import java.util.UUID;

import model.Model;
import model.RelativePoint;
import model.Table;

/**
 * Test class for {@link model.Table}.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class TableTest implements Testable {

	public boolean jsonTest() {
		// prepare data
		Model.getInstance().loadEmptyProject();
		Table table = new Table(UUID.randomUUID(), new RelativePoint(UUID.randomUUID(), 0.24, 0.43233657377), 2, "comment");

		// execute tests
		String tableData = table.toJsonString();
		Table table2 = (Table) new Table().fillFromJsonString(tableData);

		// return result
		return table.equals(table2);
	}

	public boolean constructorsTest() {
		// execute tests
		boolean defaultConstructorFound = false;
		boolean copyConstructorFound = false;
		for (Constructor<?> c : Table.class.getDeclaredConstructors()) {
			defaultConstructorFound |= c.toString().equals("public " + Table.class.getName() + "()");
			copyConstructorFound |= c.toString().equals("public " + Table.class.getName() + "(" + Table.class.getName() + ")");
		}

		// return result
		return defaultConstructorFound & copyConstructorFound;
	}
}
