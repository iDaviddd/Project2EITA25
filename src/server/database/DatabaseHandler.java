package server.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashSet;

import server.ActionType;
import server.Log;
import server.Record;
import server.User;

public class DatabaseHandler {

	private String url;
	Connection conn;

	public DatabaseHandler(String url) {
		this.url = "jdbc:sqlite:" + url;
		this.conn = null;
		initializeDB();
		createTables();
	}

	private void initializeDB() {
		try {
			this.conn = DriverManager.getConnection(url);
			if (conn != null) {
				DatabaseMetaData meta = conn.getMetaData();
				System.out.println("The driver name is " + meta.getDriverName());
			}
		} catch (SQLException e) {
			System.out.println("Errorr: " + e.getMessage());
		}
	}

	private void createTables() {
		// SQL statement for creating a new table
		String sql1 = "CREATE TABLE IF NOT EXISTS users (\n" + " id integer PRIMARY KEY, \n" + " name VARCHAR(255),\n"
				+ " role VARCHAR(20) NOT NULL,\n" + " personal_number VARCHAR(20) NOT NULL,\n"
				+ " password text NOT NULL,\n" + " salt text NOT NULL,\n" + " division VARCHAR(20)\n" + ");";
		String sql2 = "CREATE TABLE IF NOT EXISTS records (\n" + " record_id integer PRIMARY KEY,\n"
				+ " patient_personal_number INT NOT NULL,\n" + " doctor_personal_number INT NOT NULL,\n"
				+ " nurse_personal_number INT NOT NULL,\n" + " division VARCHAR(20) NOT NULL,\n"
				+ " record VARCHAR(255) NOT NULL\n" + ");";
		String sql3 = "CREATE TABLE IF NOT EXISTS logs (\n" + "log_id integer PRIMARY KEY,\n"
				+ "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,\n" + "user_personal_number INT NOT NULL,\n"
				+ "record_id INT,\n" + "action_type VARCHAR(20) NOT NULL,\n" + "action VARCHAR(255) NOT NULL\n" + ");";
		try {
			Connection conn = DriverManager.getConnection(url);
			Statement stmt = conn.createStatement();
			stmt.execute(sql1);
			stmt.execute(sql2);
			stmt.execute(sql3);
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void addRecord(Record r) {
		String sql = "INSERT INTO records(patient_personal_number, doctor_personal_number, nurse_personal_number, divison, record) VALUES(?,?,?,?,?)";

		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, r.getPatientPersonalNumber());
			pstmt.setString(2, r.getDoctorPersonalNumber());
			pstmt.setString(3, r.getNursePersonalNumber());
			pstmt.setString(4, r.getDivision());
			pstmt.setString(5, r.getRecord());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void addLog(Log log) {

		String sql = "INSERT INTO logs(user_personal_number, record_id, action_type, action) VALUES(?,?,?,?)";

		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, log.getPersonalNumber());
			pstmt.setInt(2, log.getRecordId());
			pstmt.setString(3, log.getActionType().toString());
			pstmt.setString(4, log.getAction());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}

	}

	public void addUser(User user) {
		String sql = "INSERT INTO users(name, role, personal_number, password, salt, division) VALUES(?,?,?,?,?,?)";

		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user.getName());
			pstmt.setString(2, user.getRole());
			pstmt.setString(3, user.getPersonalNumber());
			pstmt.setString(4, user.getPassword());
			pstmt.setString(5, user.getSalt());
			if (user.getDivision() != "") {
				pstmt.setString(6, user.getDivision());
			} else {
				pstmt.setString(6, null);
			}
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void deleteUser(int id) {
		// Specify sql command
		String sql = "DELETE FROM users WHERE id='" + id + "';";

		try {
			// Execute command
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public HashSet<User> findUsers(String column, String search_term) {
		HashSet<User> set = new HashSet<User>();

		String sql = "SELECT * FROM users WHERE " + column + "='" + search_term + "';";

		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String role = rs.getString("role");
				String personal_number = rs.getString("personal_number");
				String password = rs.getString("password");
				String salt = rs.getString("salt");
				String division = rs.getString("division");
				User user = new User(name, role, personal_number, password, salt, division, id);
				set.add(user);
				System.out.println(user);
			}

		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return set;
	}

	public HashSet<Record> findRecords(String column, String search_term) {
		HashSet<Record> set = new HashSet<Record>();

		String sql = "SELECT * FROM records WHERE " + column + "='" + search_term + "';";

		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String patient_personal_number = rs.getString("patient_personal_number");
				String doctor_personal_number = rs.getString("doctor_personal_number");
				String nurse_personal_number = rs.getString("nurse_personal_number");
				String division = rs.getString("division");
				String record = rs.getString("record");
				Record r = new Record(patient_personal_number, doctor_personal_number, nurse_personal_number, division,
						record);
				set.add(r);
				System.out.println(r);
			}

		} catch (SQLException e) {
			System.out.println("Error" + e.getMessage());
		}
		return set;
	}

	public HashSet<Log> findLogs(String column, String search_term) {
		HashSet<Log> set = new HashSet<Log>();

		String sql = "SELECT * FROM logs WHERE " + column + "='" + search_term + "';";

		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				int id = rs.getInt("id");
				String personal_number = rs.getString("personal_number");
				int record_id = rs.getInt("record_id");
				Integer log_id = rs.getInt("log_id");
				String action_type = rs.getString("action_type");
				String action = rs.getString("action");
				Timestamp timestamp = rs.getTimestamp("timestamp");
				Log log = new Log(personal_number, record_id, log_id, action_type, action, timestamp);
				set.add(log);
				System.out.println(log);
			}

		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return set;
	}

	public void printAllUsers() {
		String sql = "SELECT * FROM users";

		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			// Loop through the result set
			while (rs.next()) {
				System.out.println(rs.getInt("id") + "\t" + rs.getString("name") + "\t" + rs.getString("role") + "\t"
						+ rs.getString("personal_number") + "\t" + rs.getString("password") + "\t"
						+ rs.getString("salt") + "\t" + rs.getString("division"));
			}
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

}
