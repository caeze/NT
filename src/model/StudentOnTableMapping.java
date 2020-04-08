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
	private LazyAObject<Student> _002_student;
	private LazyAObject<Table> _003_table;
	private int _004_positionOnTable;

	public StudentOnTableMapping() {
	}

	public StudentOnTableMapping(UUID uuid, LazyAObject<Student> student, LazyAObject<Table> table, int positionOnTable) {
		this._001_uuid = uuid;
		this._002_student = student;
		this._003_table = table;
		this._004_positionOnTable = positionOnTable;
	}

	public StudentOnTableMapping(StudentOnTableMapping other) {
		this._001_uuid = other._001_uuid;
		this._002_student = other._002_student;
		this._003_table = other._003_table;
		this._004_positionOnTable = other._004_positionOnTable;
	}

	public UUID getUuid() {
		return _001_uuid;
	}

	public void setUuid(UUID uuid) {
		this._001_uuid = uuid;
	}

	public LazyAObject<Student> getStudent() {
		return _002_student;
	}

	public void setStudent(LazyAObject<Student> student) {
		this._002_student = student;
	}

	public LazyAObject<Table> getTable() {
		return _003_table;
	}

	public void setTable(LazyAObject<Table> table) {
		this._003_table = table;
	}

	public int getPositionOnTable() {
		return _004_positionOnTable;
	}

	public void setPositionOnTable(int positionOnTable) {
		this._004_positionOnTable = positionOnTable;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_001_uuid == null) ? 0 : _001_uuid.hashCode());
		result = prime * result + ((_002_student == null) ? 0 : _002_student.hashCode());
		result = prime * result + ((_003_table == null) ? 0 : _003_table.hashCode());
		result = prime * result + _004_positionOnTable;
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
		if (_002_student == null) {
			if (other._002_student != null)
				return false;
		} else if (!_002_student.equals(other._002_student))
			return false;
		if (_003_table == null) {
			if (other._003_table != null)
				return false;
		} else if (!_003_table.equals(other._003_table))
			return false;
		if (_004_positionOnTable != other._004_positionOnTable)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "StudentOnTableMapping [_001_uuid=" + _001_uuid + ", _002_student=" + _002_student + ", _003_table=" + _003_table + ", _004_positionOnTable=" + _004_positionOnTable + "]";
	}
}
