package server.network;

import javax.net.ssl.*;
import java.io.*;
import java.net.ServerSocket;

import com.google.gson.Gson;
import utility.*;


public class NetworkHandler implements Runnable {
    private ServerSocket serverSocket = null;
    private static int numConnectedClients = 0;

    public NetworkHandler(ServerSocket ss) throws IOException {
        serverSocket = ss;
        newListener();
    }

    public void run() {
        try {

            PrintWriter out = null;
            BufferedReader in = null;

            SSLSocket socket = (SSLSocket) serverSocket.accept();
            newListener();
            numConnectedClients++;
            System.out.println("client connected");
            System.out.println(numConnectedClients + " concurrent connection(s)\n");

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Communicator communicator = new Communicator(in, out);
            User user = null;
            while(user == null){
                user = LoginHandler.login(communicator);
            }


            while (user != null) {
                Request request = communicator.receive();
                RequestHandler.processRequest(request, user, communicator);
            }

            in.close();
            out.close();
            socket.close();
            numConnectedClients--;
            System.out.println("client disconnected");
            System.out.println(numConnectedClients + " concurrent connection(s)\n");
        } catch (IOException e) {
            System.out.println("Client died: " + e.getMessage());
            e.printStackTrace();
            return;
        }
    }

    private void newListener() {
        (new Thread(this)).start();
    } // calls run()


}
