package server.network;

import de.taimos.totp.TOTP;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import server.ServerMain;
import utility.Communicator;
import utility.Hasher;
import utility.User;

import java.util.List;

public class LoginHandler {

    static boolean login(Communicator communicator){
        boolean authenticated = false;
        while (true) {
            String username = communicator.receive();
            String password = communicator.receive();
            String OTP = communicator.receive();

            System.out.println("username: " + username);
            System.out.println("password: " + password);
            System.out.println("-------------");
            if(LoginHandler.testUserCreditials(username,password,OTP)){
                communicator.send("Authenticated");
                System.out.println("Authenticated");
                return true;
            }
            else{
                communicator.send("Error");
                return false;
            }
        }
    }


    static boolean testUserCreditials(String username, String password, String OTP){
        List<User> users = ServerMain.databaseHandler.findUsers("personal_number", username);
        if(users.size() != 1) return false;

        User user = users.get(0);
        System.out.println("OTP: " + getTOTPCode(user.getOtpSecret()));

        String hashedPassword = Hasher.HashPassword(password,user.getSalt());
        if(!user.getPassword().equals(hashedPassword))
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
