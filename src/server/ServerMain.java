package server;

import server.database.DatabaseHandler;

public class ServerMain {
	
	public static void main(String[] args) {
		DatabaseHandler dbh = new DatabaseHandler("database");
		dbh.printAllUsers();
	}

}
