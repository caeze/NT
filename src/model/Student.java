package model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import console.Log;
import nt.NT;
import view.img.ImageStore;

/**
 * Data class for a student.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class Student extends AObject {
	
	private static List<String> MEMBERS = Arrays.asList("uuid", "firstName", "lastName", "dateOfBirth", "email", "mobilePhone", "comment", "image");

	private UUID uuid;
	private String firstName;
	private String lastName;
	private Date dateOfBirth;
	private String email;
	private String mobilePhone;
	private String comment;
	private byte[] image;

	public Student(UUID uuid, String firstName, String lastName, Date dateOfBirth, String email, String mobilePhone, String comment, byte[] image) {
		this.uuid = uuid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.mobilePhone = mobilePhone;
		this.comment = comment;
		this.image = image;
	}

	public Student(Student other) {
		this.uuid = other.getUuid();
		this.firstName = other.getFirstName();
		this.lastName = other.getLastName();
		this.dateOfBirth = other.getDateOfBirth();
		this.email = other.getEmail();
		this.mobilePhone = other.getMobilePhone();
		this.comment = other.getComment();
		this.image = new byte[other.getImage().length];
		System.arraycopy(other.getImage(), 0, this.image, 0, other.getImage().length);
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public static Student getDummyStudent() {
		byte[] bytes = ImageStore.getBytesFromImage(ImageStore.getScaledImage(ImageStore.getImageIcon(""), NT.STUDENT_IMAGE_WIDTH, NT.STUDENT_IMAGE_HEIGHT));
		return new Student(UUID.randomUUID(), "DUMMY_STUDENT_firstName", "DUMMY_STUDENT_lastName", new Date(), "DUMMY_STUDENT_email", "mobilePhone", "DUMMY_STUDENT_comment", bytes);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateOfBirth == null) ? 0 : dateOfBirth.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + Arrays.hashCode(image);
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((mobilePhone == null) ? 0 : mobilePhone.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
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
		if (dateOfBirth == null) {
			if (other.dateOfBirth != null)
				return false;
		} else if (!dateOfBirth.equals(other.dateOfBirth))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (!Arrays.equals(image, other.image))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (mobilePhone == null) {
			if (other.mobilePhone != null)
				return false;
		} else if (!mobilePhone.equals(other.mobilePhone))
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Student [uuid=" + uuid + ", firstName=" + firstName + ", lastName=" + lastName + ", dateOfBirth=" + dateOfBirth + ", email=" + email + ", mobilePhone=" + mobilePhone + ", comment=" + comment + ", image=" + Arrays.toString(image) + "]";
	}

	public static String toJsonString(Student student) {
		return toJsonObject(student).toJSONString();
	}

	@SuppressWarnings("unchecked")
	public static JSONObject toJsonObject(Student student) {
		JSONObject objToReturn = new JSONObject();
		objToReturn.put("uuid", student.getUuid().toString());
		objToReturn.put("firstName", student.getFirstName());
		objToReturn.put("lastName", student.getLastName());
		objToReturn.put("dateOfBirth", NT.SDF_FOR_PERSISTING.format(student.getDateOfBirth()));
		objToReturn.put("email", student.getEmail());
		objToReturn.put("mobilePhone", student.getMobilePhone());
		objToReturn.put("comment", student.getComment());
		objToReturn.put("image", dataToCompressedBase64String(student.getImage()));
		return objToReturn;
	}

	public static Student fromJsonString(String jsonString) {
		try {
			return fromJsonObject((JSONObject) new JSONParser().parse(jsonString));
		} catch (Exception e) {
			Log.error(Student.class, "Could not parse student! " + e.getMessage());
		}
		return null;
	}

	public static Student fromJsonObject(JSONObject jsonObject) throws Exception {
		UUID uuid = UUID.fromString((String) jsonObject.get("uuid"));
		String firstName = (String) jsonObject.get("firstName");
		String lastName = (String) jsonObject.get("lastName");
		Date dateOfBirth = NT.SDF_FOR_PERSISTING.parse((String) jsonObject.get("dateOfBirth"));
		String email = (String) jsonObject.get("email");
		String mobilePhone = (String) jsonObject.get("mobilePhone");
		String comment = (String) jsonObject.get("comment");
		byte[] image = compressedBase64StringToData((String) jsonObject.get("image"));
		return new Student(uuid, firstName, lastName, dateOfBirth, email, mobilePhone, comment, image);
	}

	private static String dataToCompressedBase64String(byte[] data) {
		try {
			ByteArrayOutputStream rstBao = new ByteArrayOutputStream();
			GZIPOutputStream zos = new GZIPOutputStream(rstBao);
			zos.write(data);
			zos.close();
			return Base64.getEncoder().encodeToString(rstBao.toByteArray());
		} catch (Exception e) {
			Log.error(Student.class, "Error compressing String: " + Arrays.toString(data) + ": " + e);
		}
		return null;
	}

	private static byte[] compressedBase64StringToData(String zippedBase64Str) {
		try {
			byte[] bytes = Base64.getDecoder().decode(zippedBase64Str);
			byte[] buffer = new byte[1024];
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			GZIPInputStream zis = new GZIPInputStream(new ByteArrayInputStream(bytes));
			int len;
			while ((len = zis.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			zis.close();
			return out.toByteArray();
		} catch (Exception e) {
			Log.error(Student.class, "Error uncompressing String: " + zippedBase64Str + ": " + e);
		}
		return null;
	}

	@Override
	protected List<String> getMembers() {
		return MEMBERS;
	}
}
