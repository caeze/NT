package model;

import java.util.List;
import java.util.UUID;

/**
 * Data class for a course.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class Course extends AObject {

	private UUID _001_uuid;
	private String _002_subject;
	private String _003_grade;
	private String _004_letter;
	private LazyAObject<Room> _005_room;
	private List<StudentOnTableMapping> _006_studentOnTableMapping;
	private String _007__comment;

	public Course() {
	}

	public Course(UUID uuid, String subject, String grade, String letter, LazyAObject<Room> room, List<LazyAObject<Student>> students, List<StudentOnTableMapping> studentOnTableMapping, String comment) {
		this._001_uuid = uuid;
		this._002_subject = subject;
		this._003_grade = grade;
		this._004_letter = letter;
		this._005_room = room;
		this._006_studentOnTableMapping = studentOnTableMapping;
		this._007__comment = comment;
	}

	public Course(Course other) {
		this._001_uuid = other._001_uuid;
		this._002_subject = other._002_subject;
		this._003_grade = other._003_grade;
		this._004_letter = other._004_letter;
		this._005_room = other._005_room;
		this._006_studentOnTableMapping = other._006_studentOnTableMapping;
		this._007__comment = other._007__comment;
	}

	public UUID getUuid() {
		return _001_uuid;
	}

	public void setUuid(UUID uuid) {
		this._001_uuid = uuid;
	}

	public String getSubject() {
		return _002_subject;
	}

	public void setSubject(String subject) {
		this._002_subject = subject;
	}

	public String getGrade() {
		return _003_grade;
	}

	public void setGrade(String grade) {
		this._003_grade = grade;
	}

	public String getLetter() {
		return _004_letter;
	}

	public void setLetter(String letter) {
		this._004_letter = letter;
	}

	public LazyAObject<Room> getRoom() {
		return _005_room;
	}

	public void setRoom(LazyAObject<Room> room) {
		this._005_room = room;
	}

	public List<StudentOnTableMapping> getStudentOnTableMapping() {
		return _006_studentOnTableMapping;
	}

	public void setStudentOnTableMapping(List<StudentOnTableMapping> studentOnTableMapping) {
		this._006_studentOnTableMapping = studentOnTableMapping;
	}

	public String getComment() {
		return _007__comment;
	}

	public void setComment(String comment) {
		this._007__comment = comment;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_007__comment == null) ? 0 : _007__comment.hashCode());
		result = prime * result + ((_003_grade == null) ? 0 : _003_grade.hashCode());
		result = prime * result + ((_004_letter == null) ? 0 : _004_letter.hashCode());
		result = prime * result + ((_005_room == null) ? 0 : _005_room.hashCode());
		result = prime * result + ((_006_studentOnTableMapping == null) ? 0 : _006_studentOnTableMapping.hashCode());
		result = prime * result + ((_002_subject == null) ? 0 : _002_subject.hashCode());
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
		Course other = (Course) obj;
		if (_007__comment == null) {
			if (other._007__comment != null)
				return false;
		} else if (!_007__comment.equals(other._007__comment))
			return false;
		if (_003_grade == null) {
			if (other._003_grade != null)
				return false;
		} else if (!_003_grade.equals(other._003_grade))
			return false;
		if (_004_letter == null) {
			if (other._004_letter != null)
				return false;
		} else if (!_004_letter.equals(other._004_letter))
			return false;
		if (_005_room == null) {
			if (other._005_room != null)
				return false;
		} else if (!_005_room.equals(other._005_room))
			return false;
		if (_006_studentOnTableMapping == null) {
			if (other._006_studentOnTableMapping != null)
				return false;
		} else if (!_006_studentOnTableMapping.equals(other._006_studentOnTableMapping))
			return false;
		if (_002_subject == null) {
			if (other._002_subject != null)
				return false;
		} else if (!_002_subject.equals(other._002_subject))
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
		return "Course [uuid=" + _001_uuid + ", subject=" + _002_subject + ", grade=" + _003_grade + ", letter=" + _004_letter + ", room=" + _005_room + ", studentOnTableMapping=" + _006_studentOnTableMapping + ", comment=" + _007__comment + "]";
	}
}
