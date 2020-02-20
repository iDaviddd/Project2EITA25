package server;

import java.util.Scanner;

import server.database.DatabaseHandler;

public class ServerMain {
	
	private static boolean running;
	
	public static void main(String[] args) {
		System.out.println("Starting server...");
		DatabaseHandler dbh = new DatabaseHandler("database");
		System.out.println("Database initialized.");
		Scanner in = new Scanner(System.in);
		running = true;
		System.out.println("Server started.");
		
		while (running) {
			String input = in.nextLine();
			switch (input) {
			case "quit":
				running = false;
				break;
			case "print":
				System.out.println("All users registered in database:");
				dbh.printAllUsers();
				break;
			case "help":
				System.out.println("--------- HELP --------");
				System.out.println("help - print this message");
				System.out.println("quit - close the server");
				System.out.println("print - print all registered users in database.");
				break;
			default:
				System.out.println("Unknown command. Type 'help' for help.");
				break;
			}
		}
		
		System.out.println("Server closed.");
	}

}
