package model;

import java.util.UUID;

/**
 * Data class for a missing reference to an AObject.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class MissingAObject<T> extends LazyAObject<T> {

	private final static UUID MISSING_OBJECT_UUID = UUID.fromString("375d298c-0070-42d6-b56f-9fb909342e9b");

	public MissingAObject() {
	}

	public MissingAObject(MissingAObject<T> other) {
	}

	public UUID getUuid() {
		return MISSING_OBJECT_UUID;
	}

	public void setUuid(UUID uuid) {
		// nop
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((MISSING_OBJECT_UUID == null) ? 0 : MISSING_OBJECT_UUID.hashCode());
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
		return true;
	}

	@Override
	public String toString() {
		return "MissingAObject<T>";
	}
}
