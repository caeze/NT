package test;

import java.util.Objects;
import java.util.UUID;

import model.AObject;

/**
 * Simple data class for testing.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class SimpleData extends AObject {

	private UUID _001_uuid;
	private int _002_a = 0;
	private String _003_b = "b";

	public SimpleData() {
	}

	public SimpleData(int a, String b) {
		this._002_a = a;
		this._003_b = b;
	}

	public SimpleData(SimpleData other) {
		this._002_a = other._002_a;
		this._003_b = other._003_b;
	}

	public UUID getUuid() {
		return _001_uuid;
	}

	public void setUuid(UUID uuid) {
		this._001_uuid = uuid;
	}

	public int getA() {
		return _002_a;
	}

	public void setA(int a) {
		this._002_a = a;
	}

	public String getB() {
		return _003_b;
	}

	public void setB(String b) {
		this._003_b = b;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 83 * hash + this._002_a;
		hash = 83 * hash + Objects.hashCode(this._003_b);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final SimpleData other = (SimpleData) obj;
		if (this._002_a != other._002_a) {
			return false;
		}
		if (!Objects.equals(this._003_b, other._003_b)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{" + "a=" + _002_a + ", b=" + _003_b + "}";
	}
}
