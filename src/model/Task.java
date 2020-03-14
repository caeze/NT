package model;

import java.util.UUID;

/**
 * Data class for a task.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class Task extends AObject {

	private UUID _001_uuid;
	private String _002_name;
	private double _003_reachableScore;
	private String _004_comment;

	public Task() {
	}

	public Task(UUID uuid, String name, double reachableScore, String comment) {
		this._001_uuid = uuid;
		this._002_name = name;
		this._003_reachableScore = reachableScore;
		this._004_comment = comment;
	}

	public Task(Task other) {
		this._001_uuid = other._001_uuid;
		this._002_name = other._002_name;
		this._003_reachableScore = other._003_reachableScore;
		this._004_comment = other._004_comment;
	}

	public UUID getUuid() {
		return _001_uuid;
	}

	public void setUuid(UUID uuid) {
		this._001_uuid = uuid;
	}

	public String getName() {
		return _002_name;
	}

	public void setName(String name) {
		this._002_name = name;
	}

	public double getReachableScore() {
		return _003_reachableScore;
	}

	public void setReachableScore(double reachableScore) {
		this._003_reachableScore = reachableScore;
	}

	public String getComment() {
		return _004_comment;
	}

	public void setComment(String comment) {
		this._004_comment = comment;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_004_comment == null) ? 0 : _004_comment.hashCode());
		result = prime * result + ((_002_name == null) ? 0 : _002_name.hashCode());
		long temp;
		temp = Double.doubleToLongBits(_003_reachableScore);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Task other = (Task) obj;
		if (_004_comment == null) {
			if (other._004_comment != null)
				return false;
		} else if (!_004_comment.equals(other._004_comment))
			return false;
		if (_002_name == null) {
			if (other._002_name != null)
				return false;
		} else if (!_002_name.equals(other._002_name))
			return false;
		if (Double.doubleToLongBits(_003_reachableScore) != Double.doubleToLongBits(other._003_reachableScore))
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
		return "Task [uuid=" + _001_uuid + ", name=" + _002_name + ", reachableScore=" + _003_reachableScore + ", comment=" + _004_comment + "]";
	}
}
