package model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import console.Log;

/**
 * Data class for a characteristic curve.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class CharacteristicCurve {

	private UUID uuid;
	private List<RelativePoint> points;
	private String comment;

	public CharacteristicCurve(UUID uuid, List<RelativePoint> points, String comment) {
		this.uuid = uuid;
		this.points = points;
		this.comment = comment;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public List<RelativePoint> getPoints() {
		return points;
	}

	public void setPoints(List<RelativePoint> points) {
		this.points = points;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((points == null) ? 0 : points.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
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
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (points == null) {
			if (other.points != null)
				return false;
		} else if (!points.equals(other.points))
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CharacteristicCurve [uuid=" + uuid + ", points=" + points + ", comment=" + comment + "]";
	}

	public static String toJsonString(CharacteristicCurve characteristicCurve) {
		return toJsonObject(characteristicCurve).toJSONString();
	}

	@SuppressWarnings("unchecked")
	public static JSONObject toJsonObject(CharacteristicCurve characteristicCurve) {
		JSONObject objToReturn = new JSONObject();
		objToReturn.put("uuid", characteristicCurve.getUuid().toString());
		JSONArray pointsJSONArray = new JSONArray();
		for (RelativePoint p : characteristicCurve.getPoints()) {
			pointsJSONArray.add(RelativePoint.toJsonObject(p));
		}
		objToReturn.put("points", pointsJSONArray);
		objToReturn.put("comment", characteristicCurve.getComment());
		return objToReturn;
	}

	public static CharacteristicCurve fromJsonString(String jsonString) {
		try {
			return fromJsonObject((JSONObject) new JSONParser().parse(jsonString));
		} catch (Exception e) {
			Log.error(CharacteristicCurve.class, "Could not parse course! " + e.getMessage());
		}
		return null;
	}

	public static CharacteristicCurve fromJsonObject(JSONObject jsonObject) throws Exception {
		UUID uuid = UUID.fromString((String) jsonObject.get("uuid"));
		List<RelativePoint> points = new ArrayList<>();
		for (Object tableJSONObject : (JSONArray) jsonObject.get("points")) {
			points.add(RelativePoint.fromJsonObject((JSONObject) tableJSONObject));
		}
		String comment = (String) jsonObject.get("comment");
		return new CharacteristicCurve(uuid, points, comment);
	}
}
