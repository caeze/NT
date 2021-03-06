package util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import console.Log;

/**
 * Class for handling of lists. E.g. making deep copies of lists. The objects in
 * the list to copy need a copy constructor.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 * @param <T> the type of the objects in the array that shall be operated on
 */
public class ListUtil<T> {

	@SuppressWarnings("unchecked")
	public List<T> makeDeepCopy(List<T> list) {
		List<T> retList = new ArrayList<>();
		if (list.isEmpty()) {
			return retList;
		}
		try {
			for (T element : list) {
				Class<?> c = element.getClass();
				T newElement = (T) c.getDeclaredConstructor(c).newInstance(element);
				retList.add(newElement);
			}
		} catch (Exception e) {
			Log.error(ListUtil.class, "Error on deep copying list, did you add a copy constructor? " + e.getMessage());
		}
		return retList;
	}

	public void addAllNoDuplicates(List<T> listToAppendTo, List<T> objectsToAdd) {
		for (T obj : objectsToAdd) {
			if (!listToAppendTo.contains(obj)) {
				listToAppendTo.add(obj);
			}
		}
	}

	public void addNoDuplicates(List<T> listToAppendTo, T objectToAdd) {
		if (!listToAppendTo.contains(objectToAdd)) {
			listToAppendTo.add(objectToAdd);
		}
	}

	public List<T> returnOnlyDuplicates(List<T> list1, List<T> list2) {
		List<T> retList = new ArrayList<>();
		for (T obj : list1) {
			if (list2.contains(obj)) {
				addNoDuplicates(retList, obj);
			}
		}
		for (T obj : list2) {
			if (list1.contains(obj)) {
				addNoDuplicates(retList, obj);
			}
		}
		return retList;
	}

	public List<T> returnOnlyDuplicates(List<T> list) {
		List<T> retList = new ArrayList<>();
		Set<T> unique = new HashSet<>();
		for (T obj : list) {
			if (!unique.add(obj)) {
				if (!retList.contains(obj)) {
					retList.add(obj);
				}
			}
		}
		return retList;
	}

	public List<T> returnContainedInBoth(List<T> list1, List<T> list2) {
		List<T> retList = new ArrayList<>();
		for (T obj : list1) {
			if (list2.contains(obj)) {
				if (!retList.contains(obj)) {
					retList.add(obj);
				}
			}
		}
		return retList;
	}

	@SuppressWarnings("unchecked")
	public T[] listToArray(List<T> listOfLists, Class<T> c) {
		int[] sizes = { listOfLists.size() };
		T[] retArray = (T[]) Array.newInstance(c, sizes);
		for (int i = 0; i < sizes[0]; i++) {
			retArray[i] = listOfLists.get(i);
		}
		return retArray;
	}

	@SuppressWarnings("unchecked")
	public T[][] listOfListsToArrayOfArrays(List<List<T>> listOfLists, Class<T> c) {
		int[] sizes = { listOfLists.size(), listOfLists.get(0).size() };
		T[][] retArray = (T[][]) Array.newInstance(c, sizes);
		for (int i = 0; i < sizes[0]; i++) {
			for (int j = 0; j < sizes[1]; j++) {
				retArray[i][j] = listOfLists.get(i).get(j);
			}
		}
		return retArray;
	}
}
