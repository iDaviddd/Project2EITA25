package client.network;

import utility.Communicator;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;


public class NetworkHandler {
    private static SSLSocket socket;
    private static PrintWriter out;
    private static BufferedReader in;
    private static Communicator communicator;

    public NetworkHandler(String host, int port){
        try { /* set up a key manager for client authentication */
            SSLSocketFactory factory = null;
            try {
                char[] password = "password".toCharArray();
                KeyStore ts = KeyStore.getInstance("JKS");
                TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
                SSLContext ctx = SSLContext.getInstance("TLS");
                ts.load(new FileInputStream("clienttruststore"), password); // truststore password (storepass);
                tmf.init(ts); // keystore can be used as truststore here
                ctx.init(null, tmf.getTrustManagers(), null);
                factory = ctx.getSocketFactory();
            } catch (Exception e) {
                throw new IOException(e.getMessage());
            }
            SSLSocket socket = (SSLSocket)factory.createSocket(host, port);
            System.out.println("\nsocket before handshake:\n" + socket + "\n");
            socket.startHandshake();
            System.out.println("secure connection established\n\n");

            out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            communicator = new Communicator(in, out);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void SendRequest(String request) throws IOException {
        communicator.send(request);
    }
}
