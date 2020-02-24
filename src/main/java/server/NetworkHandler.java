package server;

import javax.net.ServerSocketFactory;
import javax.net.ssl.*;
import javax.security.cert.X509Certificate;
import java.io.*;
import java.net.ServerSocket;
import java.security.KeyStore;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import utility.Communicator;
import de.taimos.totp.TOTP;


public class NetworkHandler implements Runnable{
    private ServerSocket serverSocket = null;
    private static int numConnectedClients = 0;

    public NetworkHandler(ServerSocket ss) throws IOException {
        serverSocket = ss;
        newListener();
    }

    public void run() {
        try {
            SSLSocket socket=(SSLSocket)serverSocket.accept();
            newListener();
            SSLSession session = socket.getSession();
            numConnectedClients++;
            System.out.println("client connected");
            //System.out.println("client name (cert subject DN field): " + subject);
            System.out.println(numConnectedClients + " concurrent connection(s)\n");

            PrintWriter out = null;
            BufferedReader in = null;
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String clientMsg = null;
            boolean authenticated = false;
            while (!authenticated) {
                Communicator communicator = new Communicator(in, out);


                String username = communicator.receive();
                String password = communicator.receive();
                String OTP = communicator.receive();

                System.out.println("username: " + username);
                System.out.println("password: " + password);
                System.out.println("-------------");
                if(login(username,password,OTP)){
                    authenticated = true;
                }
            }
            System.out.println("Authenticated");
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


    private void newListener() { (new Thread(this)).start(); } // calls run()

    private boolean login(String username, String password, String OTP){

        List<User> users = ServerMain.dbh.findUsers("personal_number", username);
        if(users.size() != 1) return false;

        User user = users.get(0);
        System.out.println("OTP: " + getTOTPCode(user.getOtpSecret()));
        if(!user.getPassword().equals(password))
            return false;

        if(!getTOTPCode(user.getOtpSecret()).equals(OTP))
            return false;

        System.out.println(users);

        return true;
    }

    private static String getTOTPCode(String secretKey) {
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(secretKey);
        String hexKey = Hex.encodeHexString(bytes);
        return TOTP.getOTP(hexKey);
    }

}
