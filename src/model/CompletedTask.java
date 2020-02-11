package model;

import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import console.Log;

/**
 * Data class for a completed task.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class CompletedTask {

	private UUID uuid;
	private UUID taskUuid;
	private UUID studentUuid;
	private double reachedScore;
	private String comment;

	public CompletedTask(UUID uuid, UUID taskUuid, UUID studentUuid, double reachedScore, String comment) {
		this.uuid = uuid;
		this.taskUuid = taskUuid;
		this.studentUuid = studentUuid;
		this.reachedScore = reachedScore;
		this.comment = comment;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public UUID getTaskUuid() {
		return taskUuid;
	}

	public void setTaskUuid(UUID taskUuid) {
		this.taskUuid = taskUuid;
	}

	public UUID getStudentUuid() {
		return studentUuid;
	}

	public void setStudentUuid(UUID studentUuid) {
		this.studentUuid = studentUuid;
	}

	public double getReachedScore() {
		return reachedScore;
	}

	public void setReachedScore(double reachedScore) {
		this.reachedScore = reachedScore;
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
		long temp;
		temp = Double.doubleToLongBits(reachedScore);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((studentUuid == null) ? 0 : studentUuid.hashCode());
		result = prime * result + ((taskUuid == null) ? 0 : taskUuid.hashCode());
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
		CompletedTask other = (CompletedTask) obj;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (Double.doubleToLongBits(reachedScore) != Double.doubleToLongBits(other.reachedScore))
			return false;
		if (studentUuid == null) {
			if (other.studentUuid != null)
				return false;
		} else if (!studentUuid.equals(other.studentUuid))
			return false;
		if (taskUuid == null) {
			if (other.taskUuid != null)
				return false;
		} else if (!taskUuid.equals(other.taskUuid))
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
		return "CompletedTask [uuid=" + uuid + ", taskUuid=" + taskUuid + ", studentUuid=" + studentUuid + ", reachedScore=" + reachedScore + ", comment=" + comment + "]";
	}

	public static String toJsonString(CompletedTask completedTask) {
		return toJsonObject(completedTask).toJSONString();
	}

	@SuppressWarnings("unchecked")
	public static JSONObject toJsonObject(CompletedTask completedTask) {
		JSONObject objToReturn = new JSONObject();
		objToReturn.put("uuid", completedTask.getUuid().toString());
		objToReturn.put("taskUuid", completedTask.getTaskUuid().toString());
		objToReturn.put("studentUuid", completedTask.getStudentUuid().toString());
		objToReturn.put("reachedScore", completedTask.getReachedScore());
		objToReturn.put("comment", completedTask.getComment());
		return objToReturn;
	}

	public static CompletedTask fromJsonString(String jsonString) {
		try {
			return fromJsonObject((JSONObject) new JSONParser().parse(jsonString));
		} catch (Exception e) {
			Log.error(CompletedTask.class, "Could not parse completed task! " + e.getMessage());
		}
		return null;
	}

	public static CompletedTask fromJsonObject(JSONObject jsonObject) {
		UUID uuid = UUID.fromString((String) jsonObject.get("uuid"));
		UUID taskUuid = UUID.fromString((String) jsonObject.get("taskUuid"));
		UUID studentUuid = UUID.fromString((String) jsonObject.get("studentUuid"));
		double reachedScore = (Double) jsonObject.get("reachedScore");
		String comment = (String) jsonObject.get("comment");
		return new CompletedTask(uuid, taskUuid, studentUuid, reachedScore, comment);
	}
}
