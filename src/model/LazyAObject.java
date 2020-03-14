package model;

import java.util.UUID;

/**
 * Data class for a reference to an AObject.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class LazyAObject<T> extends AObject {

	private UUID _001_uuid;

	public LazyAObject() {
	}

	public LazyAObject(UUID uuid) {
		this._001_uuid = uuid;
	}

	public LazyAObject(LazyAObject<T> other) {
		this._001_uuid = other._001_uuid;
	}

	public UUID getUuid() {
		return _001_uuid;
	}

	public void setUuid(UUID uuid) {
		this._001_uuid = uuid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		@SuppressWarnings("unchecked")
		LazyAObject<T> other = (LazyAObject<T>) obj;
		if (_001_uuid == null) {
			if (other._001_uuid != null)
				return false;
		} else if (!_001_uuid.equals(other._001_uuid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		@SuppressWarnings("unchecked")
		Class<T> genericClass = (Class<T>) ((AObject) getClass().getGenericSuperclass()).getClass();
		return "LazyAObject<" + genericClass.getSimpleName() +  "> [_001_uuid=" + _001_uuid + "]";
	}
}
