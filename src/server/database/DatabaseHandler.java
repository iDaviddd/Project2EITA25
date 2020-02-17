package server.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandler {

	private String dbName;
	Connection conn;

	public DatabaseHandler(String dbName) {
		this.dbName = dbName;
		this.conn = null;
		initializeDB();
	}

	public void initializeDB() {
		String url = "jdbc:sqlite:/Users/dohrbom/java-workspace/EITA25 Project 2/database/" + dbName + ".db";
		try {
			this.conn = DriverManager.getConnection(url);
			if (conn != null) {
				DatabaseMetaData meta = conn.getMetaData();
				System.out.println("The driver name is " + meta.getDriverName());
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void createTables() {
		// SQLite connection string
		String url = "jdbc:sqlite:/Users/dohrbom/java-workspace/EITA25 Project 2/database/" + dbName + ".db";

		// SQL statement for creating a new table
		String sql1 = "CREATE TABLE IF NOT EXISTS employees (\n" + " uuid integer PRIMARY KEY,\n"
				+ " name text NOT NULL,\n" + " capacity real\n" + ");";
		String sql2 = "CREATE TABLE IF NOT EXISTS users (\n" + " id integer PRIMARY KEY, \n"
				+ " firstname VARCHAR(20),\n" + " lastname VARCHAR(20),\n"
				+ " role VARCHAR(20),\n" + " username VARCHAR(20),\n" + " firstname VARCHAR(20),\n"
				+ " password text NOT NULL,\n" + " salt text NOT NULL,\n" + " division VARCHAR(20)\n" + ");";
		String sql3 = "CREATE TABLE IF NOT EXISTS records (\n" + " record_id integer PRIMARY KEY,\n"
				+ " patient_id INT NOT NULL,\n" + " doctor_id INT NOT NULL,\n"
				+ " nurse_id INT NOT NULL" + " division VARCHAR(20),\n";

		try {
			Connection conn = DriverManager.getConnection(url);
			Statement stmt = conn.createStatement();
//			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void insert(String name, double capacity) {
		String sql = "INSERT INTO employees(name, capacity) VALUES(?,?)";

		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setDouble(2, capacity);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void delete(String name) {
		String sql = "DELETE FROM employees WHERE name='" + name + "';";
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void selectAll() {
		String sql = "SELECT * FROM employees";

		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			// loop through the result set
			while (rs.next()) {
				System.out.println(rs.getInt("id") + "\t" + rs.getString("name") + "\t" + rs.getDouble("capacity"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void testDB() {
		DatabaseHandler dbh = new DatabaseHandler("records");
		dbh.selectAll();
	}

}
