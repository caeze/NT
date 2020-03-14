package model;

import java.util.List;
import java.util.UUID;

import util.ListUtil;

/**
 * Data class for a characteristic curve.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class CharacteristicCurve extends AObject {

	private UUID _001_uuid;
	private List<RelativePoint> _002_points;
	private String _003_comment;

	public CharacteristicCurve() {
	}

	public CharacteristicCurve(UUID uuid, List<RelativePoint> points, String comment) {
		this._001_uuid = uuid;
		this._002_points = points;
		this._003_comment = comment;
	}

	public CharacteristicCurve(CharacteristicCurve other) {
		this._001_uuid = other._001_uuid;
		this._002_points = new ListUtil<RelativePoint>().makeDeepCopy(other._002_points);
		this._003_comment = other._003_comment;
	}

	public UUID getUuid() {
		return _001_uuid;
	}

	public void setUuid(UUID uuid) {
		this._001_uuid = uuid;
	}

	public List<RelativePoint> getPoints() {
		return _002_points;
	}

	public void setPoints(List<RelativePoint> points) {
		this._002_points = points;
	}

	public String getComment() {
		return _003_comment;
	}

	public void setComment(String comment) {
		this._003_comment = comment;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_003_comment == null) ? 0 : _003_comment.hashCode());
		result = prime * result + ((_002_points == null) ? 0 : _002_points.hashCode());
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
		CharacteristicCurve other = (CharacteristicCurve) obj;
		if (_003_comment == null) {
			if (other._003_comment != null)
				return false;
		} else if (!_003_comment.equals(other._003_comment))
			return false;
		if (_002_points == null) {
			if (other._002_points != null)
				return false;
		} else if (!_002_points.equals(other._002_points))
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
		return "CharacteristicCurve [uuid=" + _001_uuid + ", points=" + _002_points + ", comment=" + _003_comment + "]";
	}
}
