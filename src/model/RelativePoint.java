package model;

import java.util.UUID;

/**
 * Data class for a point.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class RelativePoint extends AObject {

	private UUID _001_uuid;
	private double _002_x;
	private double _003_y;

	public RelativePoint() {
	}

	public RelativePoint(UUID uuid, double x, double y) {
		this._001_uuid = uuid;
		this._002_x = x;
		this._003_y = y;
	}

	public RelativePoint(RelativePoint other) {
		this._001_uuid = other._001_uuid;
		this._002_x = other._002_x;
		this._003_y = other._003_y;
	}

	public UUID getUuid() {
		return _001_uuid;
	}

	public void setUuid(UUID uuid) {
		this._001_uuid = uuid;
	}

	public double getX() {
		return _002_x;
	}

	public void setX(double x) {
		this._002_x = x;
	}

	public double getY() {
		return _003_y;
	}

	public void setY(double y) {
		this._003_y = y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_001_uuid == null) ? 0 : _001_uuid.hashCode());
		long temp;
		temp = Double.doubleToLongBits(_002_x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(_003_y);
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
		RelativePoint other = (RelativePoint) obj;
		if (_001_uuid == null) {
			if (other._001_uuid != null)
				return false;
		} else if (!_001_uuid.equals(other._001_uuid))
			return false;
		if (Double.doubleToLongBits(_002_x) != Double.doubleToLongBits(other._002_x))
			return false;
		if (Double.doubleToLongBits(_003_y) != Double.doubleToLongBits(other._003_y))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RelativePoint [uuid=" + _001_uuid + ", x=" + _002_x + ", y=" + _003_y + "]";
	}

	public static double absoluteToRelativeX(int x, int width, int padding) {
		double xTmp = x - padding;
		return xTmp / ((double) width - 2.0 * padding);
	}

	public static double absoluteToRelativeY(int y, int height, int padding) {
		double yTmp = y - padding;
		return yTmp / ((double) height - 2.0 * padding);
	}

	public static int relativeToAbsoluteX(double x, int width, int padding) {
		return (int) (x * (width - 2.0 * padding) + padding);
	}

	public static int relativeToAbsoluteY(double y, int height, int padding) {
		return (int) (y * (height - 2.0 * padding) + padding);
	}
}
