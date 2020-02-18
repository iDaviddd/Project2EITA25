package server.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import server.ActionType;

public class DatabaseHandler {

	private String dbName;
	Connection conn;

	public DatabaseHandler(String dbName) {
		this.dbName = dbName;
		this.conn = null;
		initializeDB();
	}

	private void initializeDB() {
		String url = "jdbc:sqlite:/Users/dohrbom/java-workspace/EITA25 Project 2/database/" + dbName + ".db";
		try {
			this.conn = DriverManager.getConnection(url);
			if (conn != null) {
				DatabaseMetaData meta = conn.getMetaData();
//				System.out.println("The driver name is " + meta.getDriverName());
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void createTables() {
		// SQLite connection string
		String url = "jdbc:sqlite:/Users/dohrbom/java-workspace/EITA25 Project 2/database/" + dbName + ".db";

		// SQL statement for creating a new table
		String sql1 = "CREATE TABLE IF NOT EXISTS users (\n" + " id integer PRIMARY KEY, \n"
				+ " firstname VARCHAR(20),\n" + " lastname VARCHAR(20),\n" + " role VARCHAR(20) NOT NULL,\n"
				+ " username VARCHAR(20) NOT NULL,\n" + " password text NOT NULL,\n" + " salt text NOT NULL,\n"
				+ " division VARCHAR(20)\n" + ");";
		String sql2 = "CREATE TABLE IF NOT EXISTS records (\n" + " record_id integer PRIMARY KEY,\n"
				+ " patient_id INT NOT NULL,\n" + " doctor_id INT NOT NULL,\n" + " nurse_id INT NOT NULL,\n"
				+ " division VARCHAR(20)\n" + ");";
		String sql3 = "CREATE TABLE IF NOT EXISTS logs (\n" + "log_id integer PRIMARY KEY,\n"
				+ "timestamp VARCHAR(20),\n" + "user_id INT NOT NULL,\n" + "record_id INT,\n"
				+ "action_type VARCHAR(20),\n" + "action VARCHAR(255)\n" + ");";
		try {
			Connection conn = DriverManager.getConnection(url);
			Statement stmt = conn.createStatement();
			stmt.execute(sql1);
			stmt.execute(sql2);
			stmt.execute(sql3);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void addLog(String timestamp, int user_id, int record_id, ActionType action_type, String action) {

		String sql = "INSERT INTO logs(timestamp, user_id, record_id, action_type, action) VALUES(?,?,?,?,?)";

		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, timestamp);
			pstmt.setInt(2, user_id);
			pstmt.setInt(3, record_id);
			pstmt.setString(4, action_type.toString());
			pstmt.setString(5, action);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}

	}

	public void addUser(String firstname, String lastname, String role, String username, String password, String salt,
			String division) {
		String sql = "INSERT INTO users(firstname, lastname, role, username, password, salt, division) VALUES(?,?,?,?,?,?,?)";

		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, firstname);
			pstmt.setString(2, lastname);
			pstmt.setString(3, role);
			pstmt.setString(4, username);
			pstmt.setString(5, password);
			pstmt.setString(6, salt);
			pstmt.setString(7, division);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void deleteUser(int id) {
		String sql = "DELETE FROM users WHERE id='" + id + "';";

		try {
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void printAllUsers() {
		String sql = "SELECT * FROM users";

		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			// loop through the result set
			while (rs.next()) {
				System.out.println(rs.getInt("id") + "\t" + rs.getString("firstname") + "\t" + rs.getString("lastname")
						+ "\t" + rs.getString("role") + "\t" + rs.getString("username") + "\t"
						+ rs.getString("password") + "\t" + rs.getString("salt") + "\t" + rs.getString("division"));
			}
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

}
