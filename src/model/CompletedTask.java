package model;

import java.util.UUID;

/**
 * Data class for a completed task.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class CompletedTask extends AObject {

	private UUID _001_uuid;
	private LazyAObject<Task> _002_task;
	private LazyAObject<Student> _003_student;
	private double _004_reachedScore;
	private String _005_comment;

	public CompletedTask() {
	}

	public CompletedTask(UUID uuid, LazyAObject<Task> task, LazyAObject<Student> student, double reachedScore, String comment) {
		this._001_uuid = uuid;
		this._002_task = task;
		this._003_student = student;
		this._004_reachedScore = reachedScore;
		this._005_comment = comment;
	}

	public CompletedTask(CompletedTask other) {
		this._001_uuid = other._001_uuid;
		this._002_task = other._002_task;
		this._003_student = other._003_student;
		this._004_reachedScore = other._004_reachedScore;
		this._005_comment = other._005_comment;
	}

	public UUID getUuid() {
		return _001_uuid;
	}

	public void setUuid(UUID uuid) {
		this._001_uuid = uuid;
	}

	public LazyAObject<Task> getTask() {
		return _002_task;
	}

	public void setTask(LazyAObject<Task> task) {
		this._002_task = task;
	}

	public LazyAObject<Student> getStudent() {
		return _003_student;
	}

	public void setStudent(LazyAObject<Student> student) {
		this._003_student = student;
	}

	public double getReachedScore() {
		return _004_reachedScore;
	}

	public void setReachedScore(double reachedScore) {
		this._004_reachedScore = reachedScore;
	}

	public String getComment() {
		return _005_comment;
	}

	public void setComment(String comment) {
		this._005_comment = comment;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_005_comment == null) ? 0 : _005_comment.hashCode());
		long temp;
		temp = Double.doubleToLongBits(_004_reachedScore);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((_003_student == null) ? 0 : _003_student.hashCode());
		result = prime * result + ((_002_task == null) ? 0 : _002_task.hashCode());
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
		CompletedTask other = (CompletedTask) obj;
		if (_005_comment == null) {
			if (other._005_comment != null)
				return false;
		} else if (!_005_comment.equals(other._005_comment))
			return false;
		if (Double.doubleToLongBits(_004_reachedScore) != Double.doubleToLongBits(other._004_reachedScore))
			return false;
		if (_003_student == null) {
			if (other._003_student != null)
				return false;
		} else if (!_003_student.equals(other._003_student))
			return false;
		if (_002_task == null) {
			if (other._002_task != null)
				return false;
		} else if (!_002_task.equals(other._002_task))
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
		return "CompletedTask [uuid=" + _001_uuid + ", task=" + _002_task + ", student=" + _003_student + ", reachedScore=" + _004_reachedScore + ", comment=" + _005_comment + "]";
	}
}
