package model;

import java.util.List;
import java.util.UUID;

import util.ListUtil;

/**
 * Data class for an exam.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class Exam extends AObject {

	private UUID _001_uuid;
	private String _002_name;
	private GradeType _003_gradeType;
	private CharacteristicCurve _004_characteristicCurve;
	private List<Task> _005_tasks;
	private List<CompletedTask> _006_completedTasks;
	private String _007_comment;

	public Exam() {
	}

	public Exam(UUID uuid, String name, GradeType gradeType, CharacteristicCurve characteristicCurve, List<Task> tasks, List<CompletedTask> completedTasks, String comment) {
		this._001_uuid = uuid;
		this._002_name = name;
		this._003_gradeType = gradeType;
		this._004_characteristicCurve = characteristicCurve;
		this._005_tasks = tasks;
		this._006_completedTasks = completedTasks;
		this._007_comment = comment;
	}

	public Exam(Exam other) {
		this._001_uuid = other._001_uuid;
		this._002_name = other._002_name;
		this._003_gradeType = other._003_gradeType;
		this._004_characteristicCurve = other._004_characteristicCurve;
		this._005_tasks = new ListUtil<Task>().makeDeepCopy(other._005_tasks);
		this._006_completedTasks = new ListUtil<CompletedTask>().makeDeepCopy(other._006_completedTasks);
		this._007_comment = other._007_comment;
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

	public GradeType getGradeType() {
		return _003_gradeType;
	}

	public void setGradeType(GradeType gradeType) {
		this._003_gradeType = gradeType;
	}

	public CharacteristicCurve getCharacteristicCurve() {
		return _004_characteristicCurve;
	}

	public void setCharacteristicCurve(CharacteristicCurve characteristicCurve) {
		this._004_characteristicCurve = characteristicCurve;
	}

	public List<Task> getTasks() {
		return _005_tasks;
	}

	public void setTasks(List<Task> tasks) {
		this._005_tasks = tasks;
	}

	public List<CompletedTask> getCompletedTasks() {
		return _006_completedTasks;
	}

	public void setCompletedTasks(List<CompletedTask> completedTasks) {
		this._006_completedTasks = completedTasks;
	}

	public String getComment() {
		return _007_comment;
	}

	public void setComment(String comment) {
		this._007_comment = comment;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_004_characteristicCurve == null) ? 0 : _004_characteristicCurve.hashCode());
		result = prime * result + ((_007_comment == null) ? 0 : _007_comment.hashCode());
		result = prime * result + ((_006_completedTasks == null) ? 0 : _006_completedTasks.hashCode());
		result = prime * result + ((_003_gradeType == null) ? 0 : _003_gradeType.hashCode());
		result = prime * result + ((_002_name == null) ? 0 : _002_name.hashCode());
		result = prime * result + ((_005_tasks == null) ? 0 : _005_tasks.hashCode());
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
		Exam other = (Exam) obj;
		if (_004_characteristicCurve == null) {
			if (other._004_characteristicCurve != null)
				return false;
		} else if (!_004_characteristicCurve.equals(other._004_characteristicCurve))
			return false;
		if (_007_comment == null) {
			if (other._007_comment != null)
				return false;
		} else if (!_007_comment.equals(other._007_comment))
			return false;
		if (_006_completedTasks == null) {
			if (other._006_completedTasks != null)
				return false;
		} else if (!_006_completedTasks.equals(other._006_completedTasks))
			return false;
		if (_003_gradeType == null) {
			if (other._003_gradeType != null)
				return false;
		} else if (!_003_gradeType.equals(other._003_gradeType))
			return false;
		if (_002_name == null) {
			if (other._002_name != null)
				return false;
		} else if (!_002_name.equals(other._002_name))
			return false;
		if (_005_tasks == null) {
			if (other._005_tasks != null)
				return false;
		} else if (!_005_tasks.equals(other._005_tasks))
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
		return "Exam [uuid=" + _001_uuid + ", name=" + _002_name + ", gradeType=" + _003_gradeType + ", characteristicCurve=" + _004_characteristicCurve + ", tasks=" + _005_tasks + ", completedTasks=" + _006_completedTasks + ", comment=" + _007_comment + "]";
	}
}
