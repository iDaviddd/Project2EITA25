package server.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHandler {

	private String dbName;

	public DatabaseHandler(String dbName) {
		this.dbName = dbName;
		initializeDB();
	}

	public void initializeDB() {
		String url = "jdbc:sqlite:/Users/dohrbom/java-workspace/EITA25 Project 2/database/" + dbName;

		try {
			Connection conn = DriverManager.getConnection(url);
			if (conn != null) {
				DatabaseMetaData meta = conn.getMetaData();
				System.out.println("The driver name is " + meta.getDriverName());
				System.out.println("A new database has been created.");
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

}
