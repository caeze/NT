package model;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import console.Log;

/**
 * Class for grade types.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class GradeType {
	public static final GradeType GRADES_1_TO_6 = new GradeType(1, 6, 0.25);
	public static final GradeType GRADES_0_TO_15 = new GradeType(15, 0, 0.33333333333333);
	public static final GradeType GRADES_1_TO_5 = new GradeType(1, 5, 0.25);

	private double bestGrade;
	private double worstGrade;
	private double spacing;

	private GradeType(double bestGrade, double worstGrade, double spacing) {
		this.bestGrade = bestGrade;
		this.worstGrade = worstGrade;
		this.spacing = spacing;
	}

	public double getBestGrade() {
		return bestGrade;
	}

	public void setBestGrade(double bestGrade) {
		this.bestGrade = bestGrade;
	}

	public double getWorstGrade() {
		return worstGrade;
	}

	public void setWorstGrade(double worstGrade) {
		this.worstGrade = worstGrade;
	}

	public double getSpacing() {
		return spacing;
	}

	public void setSpacing(double spacing) {
		this.spacing = spacing;
	}

	public int getDirection() {
		if (getWorstGrade() < getBestGrade()) {
			return -1;
		}
		return 1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(bestGrade);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(spacing);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(worstGrade);
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
		if (Double.doubleToLongBits(bestGrade) != Double.doubleToLongBits(other.bestGrade))
			return false;
		if (Double.doubleToLongBits(spacing) != Double.doubleToLongBits(other.spacing))
			return false;
		if (Double.doubleToLongBits(worstGrade) != Double.doubleToLongBits(other.worstGrade))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GradeType [bestGrade=" + bestGrade + ", worstGrade=" + worstGrade + ", spacing=" + spacing + "]";
	}

	public static String toJsonString(GradeType gradeType) {
		return toJsonObject(gradeType).toJSONString();
	}

	@SuppressWarnings("unchecked")
	public static JSONObject toJsonObject(GradeType gradeType) {
		JSONObject objToReturn = new JSONObject();
		objToReturn.put("bestGrade", gradeType.getBestGrade());
		objToReturn.put("worstGrade", gradeType.getWorstGrade());
		objToReturn.put("spacing", gradeType.getSpacing());
		return objToReturn;
	}

	public static GradeType fromJsonString(String jsonString) {
		try {
			return fromJsonObject((JSONObject) new JSONParser().parse(jsonString));
		} catch (Exception e) {
			Log.error(Task.class, "Could not parse grade type! " + e.getMessage());
		}
		return null;
	}

	public static GradeType fromJsonObject(JSONObject jsonObject) {
		double bestGrade = (Double) jsonObject.get("bestGrade");
		double worstGrade = (Double) jsonObject.get("worstGrade");
		double spacing = (Double) jsonObject.get("spacing");
		return new GradeType(bestGrade, worstGrade, spacing);
	}
}
