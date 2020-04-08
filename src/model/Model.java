package model;

import java.time.LocalDate;
import java.util.ArrayList;
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

	// undo stack oldest to newest action: s0, s1, s2, s3
	// undo stack pointer ^
	private List<Project> undoStack = new ArrayList<>();
	private int undoStackPointer = 0;

	private Project lastSavedProject = new Project();

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
		return !currentProject.equals(lastSavedProject);
	}

	public void resetLastSavedProject() {
		lastSavedProject = new Project(currentProject);
	}

	public void setLastSavedProject(Project p) {
		lastSavedProject = p;
	}

	public void resetUndoStack() {
		undoStack.clear();
		undoStackPointer = 0;
	}

	public void storeSnapshot() {
		if (!undoStack.isEmpty() && getCurrentProject().equals(undoStack.get(undoStack.size() - 1))) {
			return;
		}
		List<Project> toRemove = new ArrayList<>();
		for (int i = undoStackPointer + 1; i < undoStack.size(); i++) {
			toRemove.add(undoStack.get(i));
		}
		undoStack.removeAll(toRemove);
		undoStackPointer++;
		undoStack.add(new Project(getCurrentProject()));
		Log.debug(Model.class, "Stored a project snapshot! Size of snapshot list: " + undoStack.size());
	}

	public void undo() {
		if (!canUndo()) {
			Log.error(Model.class, "Can not undo!");
		}
		Log.debug(Model.class, "Now undoing to the last project snapshot!");
		undoStackPointer--;
		setCurrentProject(undoStack.get(undoStackPointer));
	}

	public void redo() {
		if (!canRedo()) {
			Log.error(Model.class, "Can not redo!");
		}
		Log.debug(Model.class, "Now redoing to the last project snapshot!");
		undoStackPointer++;
		setCurrentProject(undoStack.get(undoStackPointer));
	}

	public boolean canUndo() {
		return undoStackPointer > 0 && !undoStack.isEmpty();
	}

	public boolean canRedo() {
		return undoStackPointer < undoStack.size() - 1;
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
		LocalDate now = LocalDate.now();
		int AUGUST = 8;
		if (now.getMonthValue() > AUGUST) {
			year = now.getYear() + "/" + (now.getYear() + 1);
		} else {
			year = (now.getYear() - 1) + "/" + now.getYear();
		}
		currentProject = new Project(UUID.randomUUID(), students, tables, rooms, courses, year);
		resetLastSavedProject();
		resetUndoStack();
	}

	public void destroyCurrentProject() {
		currentProject = null;
		cache.clear();
		resetUndoStack();
	}

	public List<Student> getAllStudents() {
		return getCurrentProject().getStudents();
	}

	public List<Course> getAllCourses() {
		return getCurrentProject().getCourses();
	}

	public List<Room> getAllRooms() {
		return getCurrentProject().getRooms();
	}

	public List<Table> getAllTables() {
		List<Table> allTables = new ArrayList<>();
		for (Room r : getAllRooms()) {
			allTables.addAll(r.getTables());
		}
		return allTables;
	}

	public Student addStudent(Student student) {
		getCurrentProject().getStudents().add(student);
		return student;
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
		if (AObject.isMissingAObject(lazyAObject)) {
			return (T) new MissingAObject<T>();
		}
		if (cacheContains(lazyAObject.getUuid())) {
			return (T) returnFromCache(lazyAObject.getUuid());
		}
		AObjectToListMapping o = findObjectWithUuid(lazyAObject.getUuid());
		if (o != null) {
			AObject obj = o.object;
			registerToCache(obj.getUuid(), obj);
			return (T) obj;
		}
		return null;
	}

	private AObjectToListMapping findObjectWithUuid(UUID uuid) {
		AObjectToListMapping objToReturn = null;
		objToReturn = warnIfBothSetAndReturnDefault(findObjectWithUuidInList(getAllCourses(), uuid), objToReturn);
		objToReturn = warnIfBothSetAndReturnDefault(findObjectWithUuidInList(getAllRooms(), uuid), objToReturn);
		objToReturn = warnIfBothSetAndReturnDefault(findObjectWithUuidInList(getAllStudents(), uuid), objToReturn);
		objToReturn = warnIfBothSetAndReturnDefault(findObjectWithUuidInList(getAllTables(), uuid), objToReturn);

		if (objToReturn == null) {
			Log.error(Model.class, "Could not find an object in the model that has UUID: " + uuid);
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
