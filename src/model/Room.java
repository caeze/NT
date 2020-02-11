package model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import console.Log;

/**
 * Data class for a room.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class Room {

	private UUID uuid;
	private String name;
	private List<Table> tables;
	private String comment;

	public Room(UUID uuid, String name, List<Table> tables, String comment) {
		super();
		this.uuid = uuid;
		this.name = name;
		this.tables = tables;
		this.comment = comment;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Table> getTables() {
		return tables;
	}

	public void setTables(List<Table> tables) {
		this.tables = tables;
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
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((tables == null) ? 0 : tables.hashCode());
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
		Room other = (Room) obj;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (tables == null) {
			if (other.tables != null)
				return false;
		} else if (!tables.equals(other.tables))
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
		return "Room [uuid=" + uuid + ", name=" + name + ", tables=" + tables + ", comment=" + comment + "]";
	}

	public static String toJsonString(Room table) {
		return toJsonObject(table).toJSONString();
	}

	@SuppressWarnings("unchecked")
	public static JSONObject toJsonObject(Room room) {
		JSONObject objToReturn = new JSONObject();
		objToReturn.put("uuid", room.getUuid().toString());
		objToReturn.put("name", room.getName());
		JSONArray tablesJSONArray = new JSONArray();
		for (Table t : room.getTables()) {
			tablesJSONArray.add(Table.toJsonObject(t));
		}
		objToReturn.put("tables", tablesJSONArray);
		objToReturn.put("comment", room.getComment());
		return objToReturn;
	}

	public static Room fromJsonString(String jsonString) {
		try {
			return fromJsonObject((JSONObject) new JSONParser().parse(jsonString));
		} catch (Exception e) {
			Log.error(Table.class, "Could not parse room! " + e.getMessage());
		}
		return null;
	}

	public static Room fromJsonObject(JSONObject jsonObject) throws Exception {
		UUID uuid = UUID.fromString((String) jsonObject.get("uuid"));
		String name = (String) jsonObject.get("name");
		List<Table> tables = new ArrayList<>();
		for (Object tableJSONObject : (JSONArray) jsonObject.get("tables")) {
			tables.add(Table.fromJsonObject((JSONObject) tableJSONObject));
		}
		String comment = (String) jsonObject.get("comment");
		return new Room(uuid, name, tables, comment);
	}
}
