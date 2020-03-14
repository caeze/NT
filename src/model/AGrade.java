package model;

import java.util.Date;
import java.util.UUID;

/**
 * Data class for a grade.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public abstract class AGrade extends AObject {
	protected UUID _001_uuid;
	protected LazyAObject<Course> _002_course;
	protected LazyAObject<Student> _003_student;
	protected Date _004_date;
	protected double _005_grade;
	protected String _006_name;
	protected String _007_comment;

	public AGrade() {
	}

	public AGrade(UUID uuid, LazyAObject<Course> course, LazyAObject<Student> student, Date date, double grade, String name, String comment) {
		this._001_uuid = uuid;
		this._002_course = course;
		this._003_student = student;
		this._004_date = date;
		this._005_grade = grade;
		this._006_name = name;
		this._007_comment = comment;
	}

	public AGrade(AGrade other) {
		this._001_uuid = other._001_uuid;
		this._002_course = other._002_course;
		this._003_student = other._003_student;
		this._004_date = other._004_date;
		this._005_grade = other._005_grade;
		this._006_name = other._006_name;
		this._007_comment = other._007_comment;
	}

	public UUID getUuid() {
		return _001_uuid;
	}

	public void setUuid(UUID uuid) {
		this._001_uuid = uuid;
	}

	public LazyAObject<Course> getCourse() {
		return _002_course;
	}

	public void setCourse(LazyAObject<Course> course) {
		this._002_course = course;
	}

	public LazyAObject<Student> getStudent() {
		return _003_student;
	}

	public void setStudent(LazyAObject<Student> student) {
		this._003_student = student;
	}

	public Date getDate() {
		return _004_date;
	}

	public void setDate(Date date) {
		this._004_date = date;
	}

	public double getGrade() {
		return _005_grade;
	}

	public void setGrade(double grade) {
		this._005_grade = grade;
	}

	public String getName() {
		return _006_name;
	}

	public void setName(String name) {
		this._006_name = name;
	}

	public String getComment() {
		return _007_comment;
	}

	public void setComment(String comment) {
		this._007_comment = comment;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_007_comment == null) ? 0 : _007_comment.hashCode());
		result = prime * result + ((_002_course == null) ? 0 : _002_course.hashCode());
		result = prime * result + ((_004_date == null) ? 0 : _004_date.hashCode());
		long temp;
		temp = Double.doubleToLongBits(_005_grade);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((_006_name == null) ? 0 : _006_name.hashCode());
		result = prime * result + ((_003_student == null) ? 0 : _003_student.hashCode());
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
		AGrade other = (AGrade) obj;
		if (_007_comment == null) {
			if (other._007_comment != null)
				return false;
		} else if (!_007_comment.equals(other._007_comment))
			return false;
		if (_002_course == null) {
			if (other._002_course != null)
				return false;
		} else if (!_002_course.equals(other._002_course))
			return false;
		if (_004_date == null) {
			if (other._004_date != null)
				return false;
		} else if (!_004_date.equals(other._004_date))
			return false;
		if (Double.doubleToLongBits(_005_grade) != Double.doubleToLongBits(other._005_grade))
			return false;
		if (_006_name == null) {
			if (other._006_name != null)
				return false;
		} else if (!_006_name.equals(other._006_name))
			return false;
		if (_003_student == null) {
			if (other._003_student != null)
				return false;
		} else if (!_003_student.equals(other._003_student))
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
		return getClass().getSimpleName() + " [uuid=" + _001_uuid + ", course=" + _002_course + ", student=" + _003_student + ", date=" + _004_date + ", grade=" + _005_grade + ", name=" + _006_name + ", comment=" + _007_comment + "]";
	}
}
