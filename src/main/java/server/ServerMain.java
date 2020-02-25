package server;

import server.database.DatabaseHandler;
import server.network.NetworkHandler;
import server.network.SocketFactory;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class ServerMain {

	public static DatabaseHandler databaseHandler;
	
	private static boolean running;
	// /Users/dohrbom/java-workspace/EITA25_Project_2/database/database.db
	
	public static void main(String[] args) {
		System.out.println("Starting server...");
		
		int port = 9876;
		String type = "TLS";

		Scanner in = new Scanner(System.in);

		System.out.print("Enter path to database file: ");
		String pathToDataBase = in.nextLine();

		System.out.print("Enter password to keystore: ");
		String KeyStorePassword = in.nextLine();

		databaseHandler = new DatabaseHandler(pathToDataBase);
		System.out.println("Database initialized.");

		try {
			ServerSocketFactory ssf = SocketFactory.getServerSocketFactory(type, KeyStorePassword);
			ServerSocket ss = ssf.createServerSocket(port);
			new NetworkHandler(ss);
			((SSLServerSocket)ss).setNeedClientAuth(false); // disables client authentication
		} catch (IOException e) {
			System.out.println("Unable to start Server: " + e.getMessage());
			e.printStackTrace();
		}
		System.out.println("Server started.");

		running = true;
		while (running) {
			running = MenuHandler.printMenu(in, databaseHandler);
		}

		in.close();
		System.out.println("Server closed.");
	}

}
