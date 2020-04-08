package model;

import java.util.List;
import java.util.UUID;

import util.ListUtil;

/**
 * Data class for a project.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class Project extends AObject {

	private UUID _001_uuid;
	private List<Student> _002_students;
	private List<Room> _003_rooms;
	private List<Course> _004_courses;
	private String _005_year;

	public Project() {
	}

	public Project(UUID uuid, List<Student> students, List<Table> tables, List<Room> rooms, List<Course> courses, String year) {
		this._001_uuid = uuid;
		this._002_students = students;
		this._003_rooms = rooms;
		this._004_courses = courses;
		this._005_year = year;
	}

	public Project(Project other) {
		this._001_uuid = other._001_uuid;
		this._002_students = new ListUtil<Student>().makeDeepCopy(other._002_students);
		this._003_rooms = new ListUtil<Room>().makeDeepCopy(other._003_rooms);
		this._004_courses = new ListUtil<Course>().makeDeepCopy(other._004_courses);
		this._005_year = other._005_year;
	}

	public UUID getUuid() {
		return _001_uuid;
	}

	public void setUuid(UUID uuid) {
		this._001_uuid = uuid;
	}

	public List<Student> getStudents() {
		return _002_students;
	}

	public void setStudents(List<Student> students) {
		this._002_students = students;
	}

	public List<Room> getRooms() {
		return _003_rooms;
	}

	public void setRooms(List<Room> rooms) {
		this._003_rooms = rooms;
	}

	public List<Course> getCourses() {
		return _004_courses;
	}

	public void setCourses(List<Course> course) {
		this._004_courses = course;
	}

	public String getYear() {
		return _005_year;
	}

	public void setYear(String year) {
		this._005_year = year;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_005_year == null) ? 0 : _005_year.hashCode());
		result = prime * result + ((_004_courses == null) ? 0 : _004_courses.hashCode());
		result = prime * result + ((_003_rooms == null) ? 0 : _003_rooms.hashCode());
		result = prime * result + ((_002_students == null) ? 0 : _002_students.hashCode());
		result = prime * result + ((_001_uuid == null) ? 0 : _001_uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Project other = (Project) obj;
		if (_005_year == null) {
			if (other._005_year != null)
				return false;
		} else if (!_005_year.equals(other._005_year))
			return false;
		if (_004_courses == null) {
			if (other._004_courses != null)
				return false;
		} else if (!_004_courses.equals(other._004_courses))
			return false;
		if (_003_rooms == null) {
			if (other._003_rooms != null)
				return false;
		} else if (!_003_rooms.equals(other._003_rooms))
			return false;
		if (_002_students == null) {
			if (other._002_students != null)
				return false;
		} else if (!_002_students.equals(other._002_students))
			return false;
		if (_001_uuid == null) {
			if (other._001_uuid != null)
				return false;
		} else if (!_001_uuid.equals(other._001_uuid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Project [uuid=" + _001_uuid + ", students=" + _002_students + ", rooms=" + _003_rooms + ", courses=" + _004_courses + ", year=" + _005_year + "]";
	}
}
