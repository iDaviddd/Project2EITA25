package server.network;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.FileInputStream;
import java.security.KeyStore;

public class SocketFactory {

    public static ServerSocketFactory getServerSocketFactory(String type, String password) {
        if (type.equals("TLS")) {
            SSLServerSocketFactory ssf = null;
            try {
                SSLContext ctx = SSLContext.getInstance("TLS");
                KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
                KeyStore ks = KeyStore.getInstance("JKS");

                ks.load(new FileInputStream("serverkeystore"), password.toCharArray());  // keystore password (storepass)

                kmf.init(ks, password.toCharArray()); // certificate password (keypass)

                ctx.init(kmf.getKeyManagers(), null, null);
                ssf = ctx.getServerSocketFactory();
                return ssf;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return ServerSocketFactory.getDefault();
        }
        return null;
    }
}
