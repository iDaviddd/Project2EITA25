package client.network;

import utility.Communicator;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;


public class NetworkHandler {
    private static SSLSocket socket;
    private static PrintWriter out;
    private static BufferedReader in;
    public static Communicator communicator;

    public NetworkHandler(String host, int port) throws InterruptedException {
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
            e.printStackTrace();
        }


        System.out.println("\nsocket before handshake:\n" + this.socket + "\n");
        boolean connected = false;
        while (!connected) {
            connected = connect(factory, host, port);
            if (!connected) {
                System.out.println("Can't connect to server. Retrying");
                Thread.sleep(5000);
            }
        }

        System.out.println("secure connection established\n\n");
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            communicator = new Communicator(in, out);
        } catch (IOException e) {
            System.out.println("Communicator failure");
            e.printStackTrace();
        }
    }

    private boolean connect(SSLSocketFactory factory, String host, int port) {
        try {
            socket = (SSLSocket) factory.createSocket(host, port);
            socket.setEnabledCipherSuites(new String[]{"TLS_DHE_DSS_WITH_AES_256_CBC_SHA256"});
            socket.startHandshake();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
