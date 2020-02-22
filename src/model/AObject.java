package model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import console.Log;
import view.util.GenericTable;

/**
 * Abstract data class superclass.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public abstract class AObject {

	protected abstract List<String> getMembers();

	public List<String> getGetters() {
		List<String> retList = new ArrayList<>();
		for (String m : getMembers()) {
			retList.add(addOnFrontCamelCase("get", m));
		}
		return retList;
	}

	public List<String> getSetters() {
		List<String> retList = new ArrayList<>();
		for (String m : getMembers()) {
			retList.add(addOnFrontCamelCase("set", m));
		}
		return retList;
	}

	public List<Class<?>> getMembersTypes() {
		List<Class<?>> retList = new ArrayList<>();
		for (String g : getGetters()) {
			try {
				Method m = getClass().getMethod(g);
				retList.add(m.getReturnType());
			} catch (Exception e) {
				Log.error(GenericTable.class, "Could not find method '" + g + "' for object " + this + "! " + e.getMessage());
			}
		}
		return retList;
	}

	private String addOnFrontCamelCase(String toAddOnFront, String str) {
		return toAddOnFront + str.substring(0, 1).toUpperCase() + str.substring(1);
	}
}
