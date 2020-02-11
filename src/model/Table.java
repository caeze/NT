package model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import console.Log;

/**
 * Data class for a table.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class Table {

	private UUID uuid;
	private RelativePoint position;
	private List<Student> students;
	private String comment;

	public Table(UUID uuid, RelativePoint position, List<Student> students, String comment) {
		this.uuid = uuid;
		this.position = position;
		this.students = students;
		this.comment = comment;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public RelativePoint getPosition() {
		return position;
	}

	public void setPosition(RelativePoint position) {
		this.position = position;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
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
		result = prime * result + ((students == null) ? 0 : students.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		result = prime * result + ((position == null) ? 0 : position.hashCode());
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
		Table other = (Table) obj;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (students == null) {
			if (other.students != null)
				return false;
		} else if (!students.equals(other.students))
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Table [uuid=" + uuid + ", position=" + position + ", students=" + students + ", comment=" + comment + "]";
	}

	public static String toJsonString(Table table) {
		return toJsonObject(table).toJSONString();
	}

	@SuppressWarnings("unchecked")
	public static JSONObject toJsonObject(Table table) {
		JSONObject objToReturn = new JSONObject();
		objToReturn.put("uuid", table.getUuid().toString());
		objToReturn.put("position", table.getPosition());
		JSONArray studentsJSONArray = new JSONArray();
		for (Student s : table.getStudents()) {
			studentsJSONArray.add(Student.toJsonObject(s));
		}
		objToReturn.put("students", studentsJSONArray);
		objToReturn.put("comment", table.getComment());
		return objToReturn;
	}

	public static Table fromJsonString(String jsonString) {
		try {
			return fromJsonObject((JSONObject) new JSONParser().parse(jsonString));
		} catch (Exception e) {
			Log.error(Table.class, "Could not parse table! " + e.getMessage());
		}
		return null;
	}

	public static Table fromJsonObject(JSONObject jsonObject) throws Exception {
		UUID uuid = UUID.fromString((String) jsonObject.get("uuid"));
		RelativePoint position = RelativePoint.fromJsonObject((JSONObject) jsonObject.get("position"));
		List<Student> students = new ArrayList<>();
		for (Object studentJSONObject : (JSONArray) jsonObject.get("students")) {
			students.add(Student.fromJsonObject((JSONObject) studentJSONObject));
		}
		String comment = (String) jsonObject.get("comment");
		return new Table(uuid, position, students, comment);
	}
}
