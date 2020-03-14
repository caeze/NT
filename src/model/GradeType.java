package model;

import java.util.UUID;

/**
 * Class for grade types.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class GradeType extends AObject {

	public static class DefaultGradeTypes {
		public static final GradeType GRADES_1_TO_6 = new GradeType(UUID.fromString("a8953375-8073-429c-856b-92f962cdb9d6"), 1, 6, 0.25);
		public static final GradeType GRADES_0_TO_15 = new GradeType(UUID.fromString("e5a75a85-7ba8-4a10-a984-624e46947415"), 15, 0, 0.33333333333333);
		public static final GradeType GRADES_1_TO_5 = new GradeType(UUID.fromString("f27a2984-198e-4f35-8960-416db73661f9"), 1, 5, 0.25);
	}

	private UUID _001_uuid;
	private double _002_bestGrade;
	private double _003_worstGrade;
	private double _004_spacing;

	public GradeType() {
	}

	public GradeType(UUID uuid, double bestGrade, double worstGrade, double spacing) {
		this._001_uuid = uuid;
		this._002_bestGrade = bestGrade;
		this._003_worstGrade = worstGrade;
		this._004_spacing = spacing;
	}

	public GradeType(GradeType other) {
		this._001_uuid = other._001_uuid;
		this._002_bestGrade = other._002_bestGrade;
		this._003_worstGrade = other._003_worstGrade;
		this._004_spacing = other._004_spacing;
	}

	public UUID getUuid() {
		return _001_uuid;
	}

	public void setUuid(UUID uuid) {
		this._001_uuid = uuid;
	}

	public double getBestGrade() {
		return _002_bestGrade;
	}

	public void setBestGrade(double bestGrade) {
		this._002_bestGrade = bestGrade;
	}

	public double getWorstGrade() {
		return _003_worstGrade;
	}

	public void setWorstGrade(double worstGrade) {
		this._003_worstGrade = worstGrade;
	}

	public double getSpacing() {
		return _004_spacing;
	}

	public void setSpacing(double spacing) {
		this._004_spacing = spacing;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(_002_bestGrade);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(_004_spacing);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((_001_uuid == null) ? 0 : _001_uuid.hashCode());
		temp = Double.doubleToLongBits(_003_worstGrade);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		GradeType other = (GradeType) obj;
		if (Double.doubleToLongBits(_002_bestGrade) != Double.doubleToLongBits(other._002_bestGrade))
			return false;
		if (Double.doubleToLongBits(_004_spacing) != Double.doubleToLongBits(other._004_spacing))
			return false;
		if (_001_uuid == null) {
			if (other._001_uuid != null)
				return false;
		} else if (!_001_uuid.equals(other._001_uuid))
			return false;
		if (Double.doubleToLongBits(_003_worstGrade) != Double.doubleToLongBits(other._003_worstGrade))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GradeType [uuid=" + _001_uuid + ", bestGrade=" + _002_bestGrade + ", worstGrade=" + _003_worstGrade + ", spacing=" + _004_spacing + "]";
	}

	public int getDirection() {
		if (getWorstGrade() < getBestGrade()) {
			return -1;
		}
		return 1;
	}
}
