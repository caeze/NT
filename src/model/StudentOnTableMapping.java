package model;

import java.util.UUID;

/**
 * Data class for a mapping on which table a student is placed.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class StudentOnTableMapping extends AObject {

	private UUID _001_uuid;
	private UUID _002_studentUuid;
	private UUID _003_tableUuid;

	public StudentOnTableMapping() {
	}

	public StudentOnTableMapping(UUID uuid, UUID studentUuid, UUID tableUuid) {
		this._001_uuid = uuid;
		this._002_studentUuid = studentUuid;
		this._003_tableUuid = tableUuid;
	}

	public StudentOnTableMapping(StudentOnTableMapping other) {
		this._001_uuid = other._001_uuid;
		this._002_studentUuid = other._002_studentUuid;
		this._003_tableUuid = other._003_tableUuid;
	}

	public UUID getUuid() {
		return _001_uuid;
	}

	public void setUuid(UUID uuid) {
		this._001_uuid = uuid;
	}

	public UUID getStudentUuid() {
		return _002_studentUuid;
	}

	public void setStudentUuid(UUID studentUuid) {
		this._002_studentUuid = studentUuid;
	}

	public UUID getTableUuid() {
		return _003_tableUuid;
	}

	public void setTableUuid(UUID tableUuid) {
		this._003_tableUuid = tableUuid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_001_uuid == null) ? 0 : _001_uuid.hashCode());
		result = prime * result + ((_002_studentUuid == null) ? 0 : _002_studentUuid.hashCode());
		result = prime * result + ((_003_tableUuid == null) ? 0 : _003_tableUuid.hashCode());
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
		StudentOnTableMapping other = (StudentOnTableMapping) obj;
		if (_001_uuid == null) {
			if (other._001_uuid != null)
				return false;
		} else if (!_001_uuid.equals(other._001_uuid))
			return false;
		if (_002_studentUuid == null) {
			if (other._002_studentUuid != null)
				return false;
		} else if (!_002_studentUuid.equals(other._002_studentUuid))
			return false;
		if (_003_tableUuid == null) {
			if (other._003_tableUuid != null)
				return false;
		} else if (!_003_tableUuid.equals(other._003_tableUuid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "StudentOnTableMapping [uuid=" + _001_uuid + ", studentUuid=" + _002_studentUuid + ", tableUuid=" + _003_tableUuid + "]";
	}
}
