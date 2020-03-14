package model;

import java.util.List;
import java.util.UUID;

import util.ListUtil;

/**
 * Data class for a room.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class Room extends AObject {

	private UUID _001_uuid;
	private String _002_name;
	private List<Table> _003_tables;
	private String _004_comment;

	public Room() {
	}

	public Room(UUID uuid, String name, List<Table> tables, String comment) {
		this._001_uuid = uuid;
		this._002_name = name;
		this._003_tables = tables;
		this._004_comment = comment;
	}

	public Room(Room other) {
		this._001_uuid = other._001_uuid;
		this._002_name = other._002_name;
		this._003_tables = new ListUtil<Table>().makeDeepCopy(other._003_tables);
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

	public List<Table> getTables() {
		return _003_tables;
	}

	public void setTables(List<Table> tables) {
		this._003_tables = tables;
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
		result = prime * result + ((_003_tables == null) ? 0 : _003_tables.hashCode());
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
		Room other = (Room) obj;
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
		if (_003_tables == null) {
			if (other._003_tables != null)
				return false;
		} else if (!_003_tables.equals(other._003_tables))
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
		return "Room [uuid=" + _001_uuid + ", name=" + _002_name + ", tables=" + _003_tables + ", comment=" + _004_comment + "]";
	}
}
