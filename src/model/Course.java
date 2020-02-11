package model;

import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import console.Log;

/**
 * Data class for a course.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class Course {

	private UUID uuid;
	private String subject; // Physik
	private String term; // WiSe 2021, SoSe 2022, etc.
	private String grade; // 7
	private String letter; // a
	private String comment;

	public Course(UUID uuid, String subject, String term, String grade, String letter, String comment) {
		this.uuid = uuid;
		this.subject = subject;
		this.term = term;
		this.grade = grade;
		this.letter = letter;
		this.comment = comment;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getLetter() {
		return letter;
	}

	public void setLetter(String letter) {
		this.letter = letter;
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
		result = prime * result + ((grade == null) ? 0 : grade.hashCode());
		result = prime * result + ((letter == null) ? 0 : letter.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		result = prime * result + ((term == null) ? 0 : term.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
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
		if (grade == null) {
			if (other.grade != null)
				return false;
		} else if (!grade.equals(other.grade))
			return false;
		if (letter == null) {
			if (other.letter != null)
				return false;
		} else if (!letter.equals(other.letter))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		if (term == null) {
			if (other.term != null)
				return false;
		} else if (!term.equals(other.term))
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Course [uuid=" + uuid + ", subject=" + subject + ", term=" + term + ", grade=" + grade + ", letter=" + letter + ", comment=" + comment + "]";
	}

	public static String toJsonString(Course course) {
		return toJsonObject(course).toJSONString();
	}

	@SuppressWarnings("unchecked")
	public static JSONObject toJsonObject(Course course) {
		JSONObject objToReturn = new JSONObject();
		objToReturn.put("uuid", course.getUuid().toString());
		objToReturn.put("subject", course.getSubject());
		objToReturn.put("term", course.getTerm());
		objToReturn.put("grade", course.getGrade());
		objToReturn.put("letter", course.getLetter());
		objToReturn.put("comment", course.getComment());
		return objToReturn;
	}

	public static Course fromJsonString(String jsonString) {
		try {
			return fromJsonObject((JSONObject) new JSONParser().parse(jsonString));
		} catch (Exception e) {
			Log.error(Course.class, "Could not parse course! " + e.getMessage());
		}
		return null;
	}

	public static Course fromJsonObject(JSONObject jsonObject) {
		UUID uuid = UUID.fromString((String) jsonObject.get("uuid"));
		String subject = (String) jsonObject.get("subject");
		String term = (String) jsonObject.get("term");
		String grade = (String) jsonObject.get("grade");
		String letter = (String) jsonObject.get("letter");
		String comment = (String) jsonObject.get("comment");
		return new Course(uuid, subject, term, grade, letter, comment);
	}
}
