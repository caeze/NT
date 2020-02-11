package model;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import console.Log;

/**
 * Data class for a point.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class RelativePoint {

	private double x;
	private double y;

	public RelativePoint(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public int getX(int width) {
        return (int) (x * width);
    }
	
	public void setX(int x, int width) {
		this.x = (double) x / (double) width;
    }
	
	public int getY(int height) {
        return (int) (y * height);
    }
	
	public void setY(int y, int height) {
		this.y = (double) y / (double) height;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
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
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}

	public static String toJsonString(RelativePoint point) {
		return toJsonObject(point).toJSONString();
	}

	@SuppressWarnings("unchecked")
	public static JSONObject toJsonObject(RelativePoint point) {
		JSONObject objToReturn = new JSONObject();
		objToReturn.put("x", point.getX());
		objToReturn.put("y", point.getY());
		return objToReturn;
	}

	public static RelativePoint fromJsonString(String jsonString) {
		try {
			return fromJsonObject((JSONObject) new JSONParser().parse(jsonString));
		} catch (Exception e) {
			Log.error(Table.class, "Could not parse point! " + e.getMessage());
		}
		return null;
	}

	public static RelativePoint fromJsonObject(JSONObject jsonObject) throws Exception {
		double x = (Double) jsonObject.get("x");
		double y = (Double) jsonObject.get("y");
		return new RelativePoint(x, y);
	}
}
