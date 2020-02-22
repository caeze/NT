package model;

import java.util.Date;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import console.Log;
import nt.NT;

/**
 * Data class for a grade.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public abstract class AGrade {
	protected UUID uuid;
	protected Course course;
	protected Student student;
	protected Date date;
	protected double grade;
	protected String name;
	protected String comment;

	public AGrade(UUID uuid, Course course, Student student, Date date, double grade, String name, String comment) {
		this.uuid = uuid;
		this.course = course;
		this.student = student;
		this.date = date;
		this.grade = grade;
		this.name = name;
		this.comment = comment;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getGrade() {
		return grade;
	}

	public void setGrade(double grade) {
		this.grade = grade;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((course == null) ? 0 : course.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		long temp;
		temp = Double.doubleToLongBits(grade);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((student == null) ? 0 : student.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
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
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (course == null) {
			if (other.course != null)
				return false;
		} else if (!course.equals(other.course))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (Double.doubleToLongBits(grade) != Double.doubleToLongBits(other.grade))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (student == null) {
			if (other.student != null)
				return false;
		} else if (!student.equals(other.student))
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [uuid=" + uuid + ", course=" + course + ", student=" + student + ", date=" + date + ", grade=" + grade + ", name=" + name + ", comment=" + comment + "]";
	}

	@SuppressWarnings("unchecked")
	protected static String toJsonString(AGrade grade) {
		JSONObject objToReturn = new JSONObject();
		objToReturn.put("uuid", grade.getUuid().toString());
		objToReturn.put("type", grade.getClass().getSimpleName());
		objToReturn.put("course", Course.toJsonObject(grade.getCourse()));
		objToReturn.put("student", Student.toJsonObject(grade.getStudent()));
		objToReturn.put("date", NT.SDF_FOR_PERSISTING.format(grade.getDate()));
		objToReturn.put("grade", grade.getGrade());
		objToReturn.put("name", grade.getName());
		objToReturn.put("comment", grade.getComment());
		return objToReturn.toJSONString();
	}

	protected static AGrade fromJsonString(String jsonString) {
		try {
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
			UUID uuid = UUID.fromString((String) jsonObject.get("uuid"));
			String type = (String) jsonObject.get("type");
			Course course = Course.fromJsonObject((JSONObject) jsonObject.get("course"));
			Student student = Student.fromJsonObject((JSONObject) jsonObject.get("student"));
			Date date = NT.SDF_FOR_PERSISTING.parse((String) jsonObject.get("date"));
			double grade = (Double) jsonObject.get("grade");
			String name = (String) jsonObject.get("name");
			String comment = (String) jsonObject.get("comment");
			if (ComputedAverageGrade.class.getSimpleName().equals(type)) {
				return new ComputedAverageGrade(uuid, course, student, date, grade, name, comment);
			} else if (ManuallyAdaptedGrade.class.getSimpleName().equals(type)) {
				return new ManuallyAdaptedGrade(uuid, course, student, date, grade, name, comment);
			} else if (NormalGrade.class.getSimpleName().equals(type)) {
				return new NormalGrade(uuid, course, student, date, grade, name, comment);
			}
		} catch (Exception e) {
			Log.error(Course.class, "Could not parse grade! " + e.getMessage());
		}
		return null;
	}
}
