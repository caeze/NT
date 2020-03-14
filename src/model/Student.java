package model;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

/**
 * Data class for a student.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class Student extends AObject {

	private UUID _001_uuid;
	private String _002_firstName;
	private String _003_lastName;
	private Date _004_dateOfBirth;
	private String _005_email;
	private String _006_mobilePhone;
	private String _007_comment;
	private byte[] _008_image;

	public Student() {
	}

	public Student(UUID uuid, String firstName, String lastName, Date dateOfBirth, String email, String mobilePhone, String comment, byte[] image) {
		this._001_uuid = uuid;
		this._002_firstName = firstName;
		this._003_lastName = lastName;
		this._004_dateOfBirth = dateOfBirth;
		this._005_email = email;
		this._006_mobilePhone = mobilePhone;
		this._007_comment = comment;
		this._008_image = image;
	}

	public Student(Student other) {
		this._001_uuid = other._001_uuid;
		this._002_firstName = other._002_firstName;
		this._003_lastName = other._003_lastName;
		this._004_dateOfBirth = other._004_dateOfBirth;
		this._005_email = other._005_email;
		this._006_mobilePhone = other._006_mobilePhone;
		this._007_comment = other._007_comment;
		this._008_image = new byte[other._008_image.length];
		System.arraycopy(other._008_image, 0, this._008_image, 0, other._008_image.length);
	}

	public UUID getUuid() {
		return _001_uuid;
	}

	public void setUuid(UUID uuid) {
		this._001_uuid = uuid;
	}

	public String getFirstName() {
		return _002_firstName;
	}

	public void setFirstName(String firstName) {
		this._002_firstName = firstName;
	}

	public String getLastName() {
		return _003_lastName;
	}

	public void setLastName(String lastName) {
		this._003_lastName = lastName;
	}

	public Date getDateOfBirth() {
		return _004_dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this._004_dateOfBirth = dateOfBirth;
	}

	public String getEmail() {
		return _005_email;
	}

	public void setEmail(String email) {
		this._005_email = email;
	}

	public String getMobilePhone() {
		return _006_mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this._006_mobilePhone = mobilePhone;
	}

	public String getComment() {
		return _007_comment;
	}

	public void setComment(String comment) {
		this._007_comment = comment;
	}

	public byte[] getImage() {
		return _008_image;
	}

	public void setImage(byte[] image) {
		this._008_image = image;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_004_dateOfBirth == null) ? 0 : _004_dateOfBirth.hashCode());
		result = prime * result + ((_005_email == null) ? 0 : _005_email.hashCode());
		result = prime * result + ((_002_firstName == null) ? 0 : _002_firstName.hashCode());
		result = prime * result + Arrays.hashCode(_008_image);
		result = prime * result + ((_003_lastName == null) ? 0 : _003_lastName.hashCode());
		result = prime * result + ((_006_mobilePhone == null) ? 0 : _006_mobilePhone.hashCode());
		result = prime * result + ((_001_uuid == null) ? 0 : _001_uuid.hashCode());
		result = prime * result + ((_007_comment == null) ? 0 : _007_comment.hashCode());
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
		Student other = (Student) obj;
		if (_004_dateOfBirth == null) {
			if (other._004_dateOfBirth != null)
				return false;
		} else if (!_004_dateOfBirth.equals(other._004_dateOfBirth))
			return false;
		if (_005_email == null) {
			if (other._005_email != null)
				return false;
		} else if (!_005_email.equals(other._005_email))
			return false;
		if (_002_firstName == null) {
			if (other._002_firstName != null)
				return false;
		} else if (!_002_firstName.equals(other._002_firstName))
			return false;
		if (!Arrays.equals(_008_image, other._008_image))
			return false;
		if (_003_lastName == null) {
			if (other._003_lastName != null)
				return false;
		} else if (!_003_lastName.equals(other._003_lastName))
			return false;
		if (_006_mobilePhone == null) {
			if (other._006_mobilePhone != null)
				return false;
		} else if (!_006_mobilePhone.equals(other._006_mobilePhone))
			return false;
		if (_001_uuid == null) {
			if (other._001_uuid != null)
				return false;
		} else if (!_001_uuid.equals(other._001_uuid))
			return false;
		if (_007_comment == null) {
			if (other._007_comment != null)
				return false;
		} else if (!_007_comment.equals(other._007_comment))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Student [uuid=" + _001_uuid + ", firstName=" + _002_firstName + ", lastName=" + _003_lastName + ", dateOfBirth=" + _004_dateOfBirth + ", email=" + _005_email + ", mobilePhone=" + _006_mobilePhone + ", comment=" + _007_comment + ", image=" + Arrays.toString(_008_image) + "]";
	}
}
