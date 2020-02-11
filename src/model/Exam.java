package model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import console.Log;

/**
 * Data class for an exam.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class Exam {

	private UUID uuid;
	private String name;
	private GradeType gradeType;
	private CharacteristicCurve characteristicCurve;
	private List<Task> tasks;
	private List<CompletedTask> completedTasks;
	private String comment;

	public Exam(UUID uuid, String name, GradeType gradeType, CharacteristicCurve characteristicCurve, List<Task> tasks, List<CompletedTask> completedTasks, String comment) {
		this.uuid = uuid;
		this.name = name;
		this.gradeType = gradeType;
		this.characteristicCurve = characteristicCurve;
		this.tasks = tasks;
		this.completedTasks = completedTasks;
		this.comment = comment;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GradeType getGradeType() {
		return gradeType;
	}

	public void setGradeType(GradeType gradeType) {
		this.gradeType = gradeType;
	}

	public CharacteristicCurve getCharacteristicCurve() {
		return characteristicCurve;
	}

	public void setCharacteristicCurve(CharacteristicCurve characteristicCurve) {
		this.characteristicCurve = characteristicCurve;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public List<CompletedTask> getCompletedTasks() {
		return completedTasks;
	}

	public void setCompletedTasks(List<CompletedTask> completedTasks) {
		this.completedTasks = completedTasks;
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
		result = prime * result + ((characteristicCurve == null) ? 0 : characteristicCurve.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((completedTasks == null) ? 0 : completedTasks.hashCode());
		result = prime * result + ((gradeType == null) ? 0 : gradeType.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((tasks == null) ? 0 : tasks.hashCode());
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
		Exam other = (Exam) obj;
		if (characteristicCurve == null) {
			if (other.characteristicCurve != null)
				return false;
		} else if (!characteristicCurve.equals(other.characteristicCurve))
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (completedTasks == null) {
			if (other.completedTasks != null)
				return false;
		} else if (!completedTasks.equals(other.completedTasks))
			return false;
		if (gradeType != other.gradeType)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (tasks == null) {
			if (other.tasks != null)
				return false;
		} else if (!tasks.equals(other.tasks))
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
		return "Exam [uuid=" + uuid + ", name=" + name + ", gradeType=" + gradeType + ", characteristicCurve=" + characteristicCurve + ", tasks=" + tasks + ", completedTasks=" + completedTasks + ", comment=" + comment + "]";
	}

	public static String toJsonString(Exam exam) {
		return toJsonObject(exam).toJSONString();
	}

	@SuppressWarnings("unchecked")
	public static JSONObject toJsonObject(Exam exam) {
		JSONObject objToReturn = new JSONObject();
		objToReturn.put("uuid", exam.getUuid().toString());
		objToReturn.put("name", exam.getName());
		objToReturn.put("gradeType", exam.getGradeType().toString());
		objToReturn.put("characteristicCurve", exam.getCharacteristicCurve());
		JSONArray tasksJSONArray = new JSONArray();
		for (Task t : exam.getTasks()) {
			tasksJSONArray.add(Task.toJsonObject(t));
		}
		objToReturn.put("tasks", tasksJSONArray);
		JSONArray completedTasksJSONArray = new JSONArray();
		for (CompletedTask t : exam.getCompletedTasks()) {
			completedTasksJSONArray.add(CompletedTask.toJsonObject(t));
		}
		objToReturn.put("completedTasks", completedTasksJSONArray);
		objToReturn.put("comment", exam.getComment());
		return objToReturn;
	}

	public static Exam fromJsonString(String jsonString) {
		try {
			return fromJsonObject((JSONObject) new JSONParser().parse(jsonString));
		} catch (Exception e) {
			Log.error(Exam.class, "Could not parse exam! " + e.getMessage());
		}
		return null;
	}

	public static Exam fromJsonObject(JSONObject jsonObject) throws Exception {
		UUID uuid = UUID.fromString((String) jsonObject.get("uuid"));
		String name = (String) jsonObject.get("name");
		GradeType gradeType = GradeType.valueOf(((String) jsonObject.get("gradeType")));
		CharacteristicCurve characteristicCurve = CharacteristicCurve.fromJsonObject((JSONObject) jsonObject.get("characteristicCurve"));
		List<Task> tasks = new ArrayList<>();
		for (Object taskJSONObject : (JSONArray) jsonObject.get("tasks")) {
			tasks.add(Task.fromJsonObject((JSONObject) taskJSONObject));
		}
		List<CompletedTask> completedTasks = new ArrayList<>();
		for (Object completedTaskJSONObject : (JSONArray) jsonObject.get("completedTasks")) {
			completedTasks.add(CompletedTask.fromJsonObject((JSONObject) completedTaskJSONObject));
		}
		String comment = (String) jsonObject.get("comment");
		return new Exam(uuid, name, gradeType, characteristicCurve, tasks, completedTasks, comment);
	}
}
