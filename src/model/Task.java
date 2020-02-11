package model;

import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import console.Log;

/**
 * Data class for a task.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class Task {

	private UUID uuid;
	private String name;
	private double reachableScore;
	private String comment;

	public Task(UUID uuid, String name, double reachableScore, String comment) {
		this.uuid = uuid;
		this.name = name;
		this.reachableScore = reachableScore;
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

	public double getReachableScore() {
		return reachableScore;
	}

	public void setReachableScore(double reachableScore) {
		this.reachableScore = reachableScore;
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
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		long temp;
		temp = Double.doubleToLongBits(reachableScore);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Task other = (Task) obj;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Double.doubleToLongBits(reachableScore) != Double.doubleToLongBits(other.reachableScore))
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
		return "Task [uuid=" + uuid + ", name=" + name + ", reachableScore=" + reachableScore + ", comment=" + comment + "]";
	}

	public static String toJsonString(Task task) {
		return toJsonObject(task).toJSONString();
	}

	@SuppressWarnings("unchecked")
	public static JSONObject toJsonObject(Task task) {
		JSONObject objToReturn = new JSONObject();
		objToReturn.put("uuid", task.getUuid().toString());
		objToReturn.put("name", task.getName());
		objToReturn.put("reachableScore", task.getReachableScore());
		objToReturn.put("comment", task.getComment());
		return objToReturn;
	}

	public static Task fromJsonString(String jsonString) {
		try {
			return fromJsonObject((JSONObject) new JSONParser().parse(jsonString));
		} catch (Exception e) {
			Log.error(Task.class, "Could not parse task! " + e.getMessage());
		}
		return null;
	}

	public static Task fromJsonObject(JSONObject jsonObject) {
		UUID uuid = UUID.fromString((String) jsonObject.get("uuid"));
		String name = (String) jsonObject.get("name");
		double reachableScore = (Double) jsonObject.get("reachableScore");
		String comment = (String) jsonObject.get("comment");
		return new Task(uuid, name, reachableScore, comment);
	}
}
