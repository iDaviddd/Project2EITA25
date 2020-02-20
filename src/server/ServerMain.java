package server;

import server.database.DatabaseHandler;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import java.io.IOException;
import java.net.ServerSocket;

public class ServerMain {
	
	public static void main(String[] args) {
		DatabaseHandler dbh = new DatabaseHandler("database");
		//dbh.printAllUsers();

		int port = 9876;
		String type = "TLS";

		try {
			ServerSocketFactory ssf = SocketFactory.getServerSocketFactory(type);
			ServerSocket ss = ssf.createServerSocket(port);
			((SSLServerSocket)ss).setNeedClientAuth(true); // enables client authentication
			new NetworkHandler(ss);
		} catch (IOException e) {
			System.out.println("Unable to start Server: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
