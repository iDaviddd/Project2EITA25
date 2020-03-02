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

	public static void main(String[] args) {
		System.out.println("Starting server...");
		
		int port = 9876;
		String type = "TLS";

		Scanner in = new Scanner(System.in);

		System.out.print("Enter path to database file: ");
		//String pathToDataBase = in.nextLine();
		String pathToDataBase = "database/database.db";

		System.out.print("Enter password to keystore: ");
		//String KeyStorePassword = in.nextLine();
		String KeyStorePassword = "password";

		databaseHandler = new DatabaseHandler(pathToDataBase);
		System.out.println("Database initialized.");

		try {
			ServerSocketFactory ssf = SocketFactory.getServerSocketFactory(type, KeyStorePassword);
			SSLServerSocket ss =  (SSLServerSocket) ssf.createServerSocket(port);
			//ss.setEnabledCipherSuites(new String[] {"TLS_ECDH_ECDSA_WITH_AES_256_GCM_SHA384"});
			ss.setNeedClientAuth(false); // disables client authentication

			new NetworkHandler(ss);
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
