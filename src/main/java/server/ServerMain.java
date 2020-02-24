package server;

import java.util.Scanner;

import server.database.DatabaseHandler;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import java.io.IOException;
import java.net.ServerSocket;

public class ServerMain {

	static DatabaseHandler dbh;
	
	private static boolean running;
	// /Users/dohrbom/java-workspace/EITA25_Project_2/database/database.db
	
	public static void main(String[] args) {
		System.out.println("Starting server...");
		
		int port = 9876;
		String type = "TLS";

		try {
			ServerSocketFactory ssf = SocketFactory.getServerSocketFactory(type);
			ServerSocket ss = ssf.createServerSocket(port);
			new NetworkHandler(ss);
			((SSLServerSocket)ss).setNeedClientAuth(false); // disables client authentication
		} catch (IOException e) {
			System.out.println("Unable to start Server: " + e.getMessage());
			e.printStackTrace();
		}
		
		Scanner in = new Scanner(System.in);
		System.out.print("Enter path to database file: ");
		String url = in.nextLine();
		dbh = new DatabaseHandler(url);
		System.out.println("Database initialized.");
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
			case "create":
				System.out.print("Name: ");
				String name = in.nextLine();
				System.out.print("Role(Government, Doctor, Nurse or Patient): ");
				String role = in.nextLine();
				System.out.print("Personal Number(XXXXXXXX-XXXX): ");
				String personal_number = in.nextLine();
				System.out.print("Password: ");
				String password = in.nextLine();
				String division = "";
				if(role.equalsIgnoreCase("doctor") || role.equalsIgnoreCase("nurse")) {
					System.out.print("Division: ");
					division = in.nextLine();
				}
				User newuser = new User(name, role, personal_number, password, "salt", division);

				dbh.addUser(newuser);
				System.out.println(name + " has been added to the system. ");
				break;
			case "find":
				System.out.print("Enter what you want to search for(name, id, perosnal_number, division)");
				String col = in.nextLine();
				System.out.print("Enter the info(If you entered name, type a name):");
				String search_term = in.nextLine();
				dbh.findUsers(col, search_term);
				break;
			case "help":
				System.out.println("--------- HELP --------");
				System.out.println("help - print this message");
				System.out.println("quit - close the server");
				System.out.println("print - print all registered users in database");
				System.out.println("create - Create a user");
				System.out.println("find - Search the user table for a user");
				break;
			default:
				System.out.println("Unknown command. Type 'help' for help.");
				break;
			}
		}
		in.close();
		System.out.println("Server closed.");
	}

}
