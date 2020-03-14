package model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import console.Log;
import nt.NT;
import view.util.GenericTable;

/**
 * Abstract data class superclass.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public abstract class AObject {

	public abstract UUID getUuid();

	public abstract void setUuid(UUID uuid);

	/**
	 * Get an ordered list of members of this object. The members have to be named
	 * as "_001_memberName1", "_002_memberName2" etc. for the ordering to work
	 * properly.
	 * 
	 * @return an ordered list of members of this object
	 */
	public List<Field> getMembers() {
		List<Field> retList = new ArrayList<>();
		for (Field f : getClass().getDeclaredFields()) {
			if (!getClass().equals(MissingAObject.class)) {
				retList.add(f);
			}
		}
		if (retList.isEmpty() && getClass().getSuperclass() != null) {
			for (Field f : getClass().getSuperclass().getDeclaredFields()) {
				if (!getClass().equals(MissingAObject.class)) {
					retList.add(f);
				}
			}
		}
		Collections.sort(retList, new Comparator<Field>() {
			@Override
			public int compare(Field f1, Field f2) {
				return f1.getName().compareTo(f2.getName());
			}
		});
		return retList;
	}

	/**
	 * Get an ordered list of member names of this object. The members have to be
	 * named as "_001_memberName1", "_002_memberName2" etc. for the ordering to work
	 * properly. Then the names are returned as "memberName1", "memberName2", etc.
	 * 
	 * @return an ordered list of member names of this object
	 */
	public List<String> getMemberNamesWithoutNumbers() {
		List<String> retList = new ArrayList<>();
		for (Field f : getMembers()) {
			retList.add(removeNumbers(f.getName()));
		}
		return retList;
	}

	/**
	 * Get an ordered list of getter method of the members of this object. The
	 * members have to be named as "_001_memberName1", "_002_memberName2" etc. for
	 * the ordering to work properly. Then the getters are returned as
	 * "getMemberName1()", "getMemberName2()", etc.
	 * 
	 * @return an ordered list of getters of this object
	 */
	public List<Method> getGetters() {
		List<Method> retList = new ArrayList<>();
		for (Field m : getMembers()) {
			retList.add(getAccessMethod("get", m.getName()));
		}
		return retList;
	}

	/**
	 * Get an ordered list of setter method of the members of this object. The
	 * members have to be named as "_001_memberName1", "_002_memberName2" etc. for
	 * the ordering to work properly. Then the getters are returned as
	 * "setMemberName1()", "setMemberName2()", etc.
	 * 
	 * @return an ordered list of setters of this object
	 */
	public List<Method> getSetters() {
		List<Method> retList = new ArrayList<>();
		for (Field m : getMembers()) {
			retList.add(getAccessMethod("set", m.getName()));
		}
		return retList;
	}

	/**
	 * Get an ordered list of the types of the members of this object. The members
	 * have to be named as "_001_memberName1", "_002_memberName2" etc. for the
	 * ordering to work properly.
	 * 
	 * @return an ordered list of member types of this object
	 */
	public List<Class<?>> getMembersTypes() {
		List<Class<?>> retList = new ArrayList<>();
		for (Field m : getMembers()) {
			try {
				retList.add(getAccessMethod("get", m.getName()).getReturnType());
			} catch (Exception e) {
				Log.error(GenericTable.class, "Could not find method '" + m + "' for object " + this + "! " + e.getMessage());
			}
		}
		return retList;
	}

	private Method getAccessMethod(String toAddOnFront, String str) {
		str = removeNumbers(str);
		String methodName = toAddOnFront + str.substring(0, 1).toUpperCase() + str.substring(1);
		try {
			if ("set".equals(toAddOnFront)) {
				for (Method m : getClass().getMethods()) {
					if (methodName.equals(m.getName())) {
						return m;
					}
				}
			}
			return getClass().getMethod(methodName);
		} catch (Exception e) {
			Log.error(AObject.class, "Error on getting " + toAddOnFront + "ter field on " + str + ". " + e.getMessage());
		}
		return null;
	}

	private String removeNumbers(String str) {
		return str.replaceAll("_.*_", "");
	}

	public String toJsonString() {
		return toJsonObject().toJSONString();
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJsonObject() {
		JSONObject objToReturn = new JSONObject();
		try {
			List<String> memberNames = getMemberNamesWithoutNumbers();
			List<Field> members = getMembers();
			List<Method> getters = getGetters();
			for (int i = 0; i < members.size(); i++) {
				Method getter = getters.get(i);
				if (isMissingAObject(getter.invoke(this))) {
					JSONArray a = new JSONArray();
					String typeName = members.get(i).getGenericType().getTypeName();
					int firstIndex = typeName.indexOf("<") + 1;
					int lastIndex = typeName.lastIndexOf(">");
					typeName = typeName.substring(firstIndex, lastIndex);
					typeName = MissingAObject.class.getName() + "<" + typeName + ">";
					a.add(typeName);
					a.add(((AObject) getter.invoke(this)).toJsonObject());
					objToReturn.put(memberNames.get(i), a);
				} else if (isLazyAObject(members.get(i).getType())) {
					JSONArray a = new JSONArray();
					String typeName = members.get(i).getGenericType().getTypeName();
					a.add(typeName);
					a.add(((AObject) getter.invoke(this)).toJsonObject());
					objToReturn.put(memberNames.get(i), a);
				} else if (isAObject(members.get(i).getType())) {
					JSONArray a = new JSONArray();
					String typeName = members.get(i).getGenericType().getTypeName();
					a.add(typeName);
					a.add(((AObject) getter.invoke(this)).toJsonObject());
					objToReturn.put(memberNames.get(i), a);
				} else if (isString(members.get(i).getType())) {
					objToReturn.put(memberNames.get(i), getter.invoke(this));
				} else if (isUuid(members.get(i).getType())) {
					objToReturn.put(memberNames.get(i), getter.invoke(this).toString());
				} else if (isDate(members.get(i).getType())) {
					objToReturn.put(memberNames.get(i), NT.SDF_FOR_PERSISTING.format(getter.invoke(this)));
				} else if (isDouble(members.get(i).getType())) {
					objToReturn.put(memberNames.get(i), getter.invoke(this));
				} else if (isInteger(members.get(i).getType())) {
					objToReturn.put(memberNames.get(i), getter.invoke(this));
				} else if (isByteArray(members.get(i).getType())) {
					objToReturn.put(memberNames.get(i), dataToCompressedBase64String((byte[]) getter.invoke(this)));
				} else if (isList(members.get(i).getType())) {
					JSONArray a = new JSONArray();
					String typeName = members.get(i).getGenericType().getTypeName();
					int firstIndex = typeName.indexOf("<") + 1;
					int lastIndex = typeName.lastIndexOf(">");
					typeName = typeName.substring(firstIndex, lastIndex);
					a.add(typeName);
					for (AObject o : (List<AObject>) getter.invoke(this)) {
						a.add(o.toJsonObject());
					}
					objToReturn.put(memberNames.get(i), a);
				} else {
					Log.error(AObject.class, "Unrecognized type, please implement: " + members.get(i).getType());
				}
			}
		} catch (Exception e) {
			Log.error(AObject.class, "Error converting object to JSON string: " + e);
		}
		return objToReturn;
	}

	public AObject fillFromJsonString(String jsonString) {
		try {
			return fillFromJsonObject((JSONObject) new JSONParser().parse(jsonString));
		} catch (Exception e) {
			Log.error(AObject.class, "Could not parse JSON object! " + e.getMessage());
		}
		return null;
	}

	public AObject fillFromJsonObject(JSONObject jsonObject) {
		try {
			List<String> memberNames = getMemberNamesWithoutNumbers();
			List<Field> members = getMembers();
			List<Method> setters = getSetters();
			for (int i = 0; i < members.size(); i++) {
				Method setter = setters.get(i);
				if (isLazyAObject(members.get(i).getType()) || isMissingAObject(members.get(i).getType())) {
					String type = null;
					for (Object jo : (JSONArray) jsonObject.get(memberNames.get(i))) {
						if (type == null) {
							type = ((String) jo).split("<")[0];
							continue;
						}
						for (Constructor<?> c : Class.forName(type).getConstructors()) {
							if (c.getParameterCount() == 0) {
								AObject obj = (AObject) c.newInstance();
								obj.fillFromJsonObject((JSONObject) jo);
								setter.invoke(this, obj);
								break;
							}
						}
					}
				} else if (isAObject(members.get(i).getType())) {
					String type = null;
					for (Object jo : (JSONArray) jsonObject.get(memberNames.get(i))) {
						if (type == null) {
							type = (String) jo;
							continue;
						}
						for (Constructor<?> c : Class.forName(type).getConstructors()) {
							if (c.getParameterCount() == 0) {
								AObject obj = (AObject) c.newInstance();
								obj.fillFromJsonObject((JSONObject) jo);
								setter.invoke(this, obj);
								break;
							}
						}
					}
				} else if (isString(members.get(i).getType())) {
					setter.invoke(this, jsonObject.get(memberNames.get(i)));
				} else if (isUuid(members.get(i).getType())) {
					setter.invoke(this, UUID.fromString((String) jsonObject.get(memberNames.get(i))));
				} else if (isDate(members.get(i).getType())) {
					setter.invoke(this, NT.SDF_FOR_PERSISTING.parse((String) jsonObject.get(memberNames.get(i))));
				} else if (isDouble(members.get(i).getType())) {
					setter.invoke(this, jsonObject.get(memberNames.get(i)));
				} else if (isInteger(members.get(i).getType())) {
					setter.invoke(this, ((Long) jsonObject.get(memberNames.get(i))).intValue());
				} else if (isByteArray(members.get(i).getType())) {
					setter.invoke(this, compressedBase64StringToData((String) jsonObject.get(memberNames.get(i))));
				} else if (isList(members.get(i).getType())) {
					List<AObject> o = new ArrayList<>();
					String type = null;
					for (Object jo : (JSONArray) jsonObject.get(memberNames.get(i))) {
						if (type == null) {
							if (!((String) jo).contains("<")) {
								type = (String) jo;
								continue;
							} else {
								type = ((String) jo).split("<")[0];
								continue;
							}
						}
						for (Constructor<?> c : Class.forName(type).getConstructors()) {
							if (c.getParameterCount() == 0) {
								AObject obj = (AObject) c.newInstance();
								obj.fillFromJsonObject((JSONObject) jo);
								o.add(obj);
								break;
							}
						}
					}
					setter.invoke(this, o);
				} else {
					Log.error(AObject.class, "Unrecognized type, please implement: " + members.get(i).getType());
				}
			}
		} catch (Exception e) {
			Log.error(AObject.class, "Error converting object to JSON string: " + e);
		}
		return this;
	}

	private static String dataToCompressedBase64String(byte[] data) {
		try {
			ByteArrayOutputStream rstBao = new ByteArrayOutputStream();
			GZIPOutputStream zos = new GZIPOutputStream(rstBao);
			zos.write(data);
			zos.close();
			return Base64.getEncoder().encodeToString(rstBao.toByteArray());
		} catch (Exception e) {
			Log.error(AObject.class, "Error compressing String: " + Arrays.toString(data) + ": " + e);
		}
		return null;
	}

	private static byte[] compressedBase64StringToData(String zippedBase64Str) {
		try {
			byte[] bytes = Base64.getDecoder().decode(zippedBase64Str);
			byte[] buffer = new byte[1024];
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			GZIPInputStream zis = new GZIPInputStream(new ByteArrayInputStream(bytes));
			int len;
			while ((len = zis.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			zis.close();
			return out.toByteArray();
		} catch (Exception e) {
			Log.error(AObject.class, "Error uncompressing String: " + zippedBase64Str + ": " + e);
		}
		return null;
	}

	public static boolean isAObject(Object obj) {
		if (obj != null && obj instanceof Class) {
			Class<?> c = (Class<?>) obj;
			c = c.getSuperclass();
			if (c == null) {
				return false;
			}
			return c.getName().equals(AObject.class.getName());
		}
		return obj != null && obj instanceof AObject;
	}

	public static boolean isString(Object obj) {
		if (obj != null && obj instanceof Class) {
			Class<?> c = (Class<?>) obj;
			return c.getName().equals(String.class.getName());
		}
		return obj != null && obj instanceof String;
	}

	public static boolean isUuid(Object obj) {
		if (obj != null && obj instanceof Class) {
			Class<?> c = (Class<?>) obj;
			return c.getName().equals(UUID.class.getName());
		}
		return obj != null && obj instanceof UUID;
	}

	public static boolean isDate(Object obj) {
		if (obj != null && obj instanceof Class) {
			Class<?> c = (Class<?>) obj;
			return c.getName().equals(Date.class.getName());
		}
		return obj != null && obj instanceof Date;
	}

	public static boolean isDouble(Object obj) {
		if (obj != null && obj instanceof Class) {
			Class<?> c = (Class<?>) obj;
			return c.getName().equals(Double.class.getName()) || c.getName().equals(double.class.getName());
		}
		return obj != null && obj instanceof Double;
	}

	public static boolean isInteger(Object obj) {
		if (obj != null && obj instanceof Class) {
			Class<?> c = (Class<?>) obj;
			return c.getName().equals(Integer.class.getName()) || c.getName().equals(int.class.getName());
		}
		return obj != null && obj instanceof Integer;
	}

	public static boolean isList(Object obj) {
		if (obj != null && obj instanceof Class) {
			Class<?> c = (Class<?>) obj;
			return c.getName().equals(List.class.getName());
		}
		return obj != null && obj instanceof List;
	}

	public static boolean isByteArray(Object obj) {
		if (obj != null && obj instanceof Class) {
			Class<?> c = (Class<?>) obj;
			return c.isArray() && c.getComponentType() == byte.class;
		}
		return obj != null && obj.getClass().isArray() && obj.getClass().getComponentType() == byte.class;
	}

	public static boolean isLazyAObject(Object obj) {
		if (obj != null && obj instanceof Class) {
			Class<?> c = (Class<?>) obj;
			return c.getName().equals(LazyAObject.class.getName());
		}
		return obj != null && obj instanceof LazyAObject;
	}

	public static boolean isMissingAObject(Object obj) {
		if (obj != null && obj instanceof Class) {
			Class<?> c = (Class<?>) obj;
			return c.getName().equals(MissingAObject.class.getName());
		}
		return obj != null && obj instanceof MissingAObject;
	}
}
