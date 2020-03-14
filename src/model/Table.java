package model;

import java.util.UUID;

import nt.NT;

/**
 * Data class for a table.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class Table extends AObject {

	private UUID _001_uuid;
	private RelativePoint _002_position;
	private int _003_numberOfPlaces;
	private String _004_comment;

	public Table() {
	}

	public Table(UUID uuid, RelativePoint position, int students, String comment) {
		this._001_uuid = uuid;
		this._002_position = position;
		this._003_numberOfPlaces = students;
		this._004_comment = comment;
	}

	public Table(Table other) {
		this._001_uuid = other._001_uuid;
		this._002_position = new RelativePoint(other._002_position);
		this._003_numberOfPlaces = other._003_numberOfPlaces;
		this._004_comment = other._004_comment;
	}

	public UUID getUuid() {
		return _001_uuid;
	}

	public void setUuid(UUID uuid) {
		this._001_uuid = uuid;
	}

	public RelativePoint getPosition() {
		return _002_position;
	}

	public void setPosition(RelativePoint position) {
		this._002_position = position;
	}

	public int getNumberOfPlaces() {
		return _003_numberOfPlaces;
	}

	public void setNumberOfPlaces(int numberOfPlaces) {
		this._003_numberOfPlaces = numberOfPlaces;
	}

	public String getComment() {
		return _004_comment;
	}

	public void setComment(String comment) {
		this._004_comment = comment;
	}

	public int getWidth(int imagesSpacing) {
		return _003_numberOfPlaces * (imagesSpacing + NT.STUDENT_IMAGE_WIDTH) - imagesSpacing;
	}

	public int getHeight() {
		return NT.STUDENT_IMAGE_HEIGHT;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_001_uuid == null) ? 0 : _001_uuid.hashCode());
		result = prime * result + ((_002_position == null) ? 0 : _002_position.hashCode());
		result = prime * result + _003_numberOfPlaces;
		result = prime * result + ((_004_comment == null) ? 0 : _004_comment.hashCode());
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
		if (_001_uuid == null) {
			if (other._001_uuid != null)
				return false;
		} else if (!_001_uuid.equals(other._001_uuid))
			return false;
		if (_002_position == null) {
			if (other._002_position != null)
				return false;
		} else if (!_002_position.equals(other._002_position))
			return false;
		if (_003_numberOfPlaces != other._003_numberOfPlaces)
			return false;
		if (_004_comment == null) {
			if (other._004_comment != null)
				return false;
		} else if (!_004_comment.equals(other._004_comment))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Table [uuid=" + _001_uuid + ", position=" + _002_position + ", numberOfPlaces=" + _003_numberOfPlaces + ", comment=" + _004_comment + "]";
	}
}
