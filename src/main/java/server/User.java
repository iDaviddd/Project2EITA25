package server;

public class User {

	private int id;
	private String name;
	private String role;
	private String personal_number;
	private String password;
	private String salt;
	private String division;

	public User(String name, String role, String personal_number, String password, String salt, String division,
			int id) {
		this.name = name;
		this.role = role;
		this.personal_number = personal_number;
		this.password = password;
		this.salt = salt;
		this.division = division;
		this.id = id;
	}

	public User(String name, String role, String personal_number, String password, String salt, String division) {
		this.name = name;
		this.role = role;
		this.personal_number = personal_number;
		this.password = password;
		this.salt = salt;
		this.division = division;
	}

	public String getName() {
		return name;
	}

	public String getRole() {
		return role;
	}

	public String getPersonalNumber() {
		return personal_number;
	}

	public String getPassword() {
		return password;
	}

	public String getSalt() {
		return salt;
	}

	public String getDivision() {
		return division;
	}

	@Override
	public String toString() {
		if (division == null) {
			return "[" + id + "] " + name + "(" + personal_number + ") with the role " + role.toLowerCase()
			+ ". \nSalt: " + salt + "\nPassword: " + password;
		} else {
			return "[" + id + "] " + name + "(" + personal_number + ") with the role " + role.toLowerCase()
					+ ". works in divison: " + division + ". \nSalt: " + salt + "\nPassword: " + password;
		}
	}

}
