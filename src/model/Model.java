package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import console.Log;

/**
 * Control of the MVC pattern.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class Model {

	private static Model instance;

	private Project currentProject = new Project();
	private Map<UUID, AObject> cache = new HashMap<>();

	private Model() {
		// hide constructor, singleton pattern
	}

	/**
	 * Get an instance, singleton pattern.
	 *
	 * @return an instance
	 */
	public static Model getInstance() {
		if (instance == null) {
			instance = new Model();
		}
		return instance;
	}

	public boolean hasUnsavedChanges() {
		return true;
	}

	public Project getCurrentProject() {
		return currentProject;
	}

	public void setCurrentProject(Project currentProject) {
		this.currentProject = currentProject;
	}

	public void loadEmptyProject() {
		List<Student> students = new ArrayList<>();
		List<Table> tables = new ArrayList<>();
		List<Room> rooms = new ArrayList<>();
		List<Course> courses = new ArrayList<>();
		String year = null;
		Date now = new Date();
		Calendar c = new GregorianCalendar();
		c.setTime(now);
		if (c.get(Calendar.MONTH) > Calendar.AUGUST) {
			year = c.get(Calendar.YEAR) + "/" + (c.get(Calendar.YEAR) + 1);
		} else {
			year = (c.get(Calendar.YEAR) - 1) + "/" + c.get(Calendar.YEAR);
		}
		currentProject = new Project(UUID.randomUUID(), students, tables, rooms, courses, year);
	}

	public void destroyCurrentProject() {
		currentProject = null;
		cache.clear();
	}

	public List<Student> getAllStudents() {
		return currentProject.getStudents();
	}

	public List<Course> getAllCourses() {
		return currentProject.getCourses();
	}

	public List<Table> getAllTables() {
		return currentProject.getTables();
	}

	public List<Room> getAllRooms() {
		return currentProject.getRooms();
	}
	
	public Student addStudent(Student student) {
		getCurrentProject().getStudents().add(student);
		return student;
	}
	
	public Table addTable(Table table) {
		getCurrentProject().getTables().add(table);
		return table;
	}
	
	public Room addRoom(Room room) {
		getCurrentProject().getRooms().add(room);
		return room;
	}
	
	public Course addCourse(Course course) {
		getCurrentProject().getCourses().add(course);
		return course;
	}

	public AObject findAObject(UUID uuid) {
		if (cacheContains(uuid)) {
			return returnFromCache(uuid);
		}
		AObject o = findObjectWithUuid(uuid).object;
		if (o == null) {
			return null;
		}
		registerToCache(o.getUuid(), o);
		return o;
	}

	@SuppressWarnings("unchecked")
	public void replaceAObject(UUID uuid, AObject newObject) {
		AObjectToListMapping o = findObjectWithUuid(uuid);
		if (o != null) {
			removeFromCache(uuid);
			int index = o.containingList.indexOf(o.object);
			o.containingList.remove(index);
			((List<AObject>) o.containingList).add(index, o.object);
		}
	}
	public void removeAObject(UUID uuid) {
		AObjectToListMapping o = findObjectWithUuid(uuid);
		if (o != null) {
			removeFromCache(uuid);
			int index = o.containingList.indexOf(o.object);
			o.containingList.remove(index);
		}
	}

	public <T> List<LazyAObject<T>> compressToLazyAObjectList(List<T> aObjectList) {
		List<LazyAObject<T>> retList = new ArrayList<>();
		for (T o : aObjectList) {
			retList.add(compressToLazyAObject(o));
		}
		return retList;
	}

	public <T> LazyAObject<T> compressToLazyAObject(T aObject) {
		return new LazyAObject<T>(((AObject) aObject).getUuid());
	}

	public <T> List<T> expandLazyAObjectList(List<LazyAObject<T>> lazyAObjectList) {
		List<T> retList = new ArrayList<>();
		for (LazyAObject<T> o : lazyAObjectList) {
			retList.add(expandLazyAObject(o));
		}
		return retList;
	}

	@SuppressWarnings("unchecked")
	public <T> T expandLazyAObject(LazyAObject<T> lazyAObject) {
		if(AObject.isMissingAObject(lazyAObject)) {
			return (T) new MissingAObject<T>();
		}
		if (cacheContains(lazyAObject.getUuid())) {
			return (T) returnFromCache(lazyAObject.getUuid());
		}
		AObject o = findObjectWithUuid(lazyAObject.getUuid()).object;
		registerToCache(o.getUuid(), o);
		return (T) o;
	}

	private AObjectToListMapping findObjectWithUuid(UUID uuid) {
		AObjectToListMapping objToReturn = null;
		objToReturn = warnIfBothSetAndReturnDefault(findObjectWithUuidInList(currentProject.getCourses(), uuid), objToReturn);
		objToReturn = warnIfBothSetAndReturnDefault(findObjectWithUuidInList(currentProject.getRooms(), uuid), objToReturn);
		objToReturn = warnIfBothSetAndReturnDefault(findObjectWithUuidInList(currentProject.getStudents(), uuid), objToReturn);
		objToReturn = warnIfBothSetAndReturnDefault(findObjectWithUuidInList(currentProject.getTables(), uuid), objToReturn);

		if (objToReturn == null) {
			Log.warning(Model.class, "Could not find an object in the model that has UUID: " + uuid);
		}
		return objToReturn;
	}

	private AObjectToListMapping findObjectWithUuidInList(List<? extends AObject> list, UUID uuid) {
		for (AObject o : list) {
			if (o.getUuid().equals(uuid)) {
				return new AObjectToListMapping(o, list);
			}
		}
		return null;
	}

	private AObjectToListMapping warnIfBothSetAndReturnDefault(AObjectToListMapping obj1, AObjectToListMapping obj2) {
		if (obj1 != null && obj2 != null) {
			Log.warning(Model.class, "Both objects are set, this is not correct! UUIDs: " + obj1.object.getUuid() + ", " + obj2.object.getUuid());
			return obj1;
		}
		if (obj1 != null) {
			return obj1;
		}
		if (obj2 != null) {
			return obj2;
		}
		return null;
	}

	private boolean cacheContains(UUID uuid) {
		return cache.containsKey(uuid);
	}

	private AObject returnFromCache(UUID uuid) {
		return cache.get(uuid);
	}

	private void removeFromCache(UUID uuid) {
		cache.remove(uuid);
	}

	private void registerToCache(UUID uuid, AObject object) {
		cache.put(uuid, object);
	}

	private class AObjectToListMapping {
		AObject object;
		List<? extends AObject> containingList;

		public AObjectToListMapping(AObject object, List<? extends AObject> containingList) {
			this.object = object;
			this.containingList = containingList;
		}
	}
}
