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
        while (true) {
            String username = communicator.receive();
            User user = findUser(username);

            communicator.send(user.getSalt());
            String challenge = User.generateSecretKey(32);
            communicator.send(challenge);
            String response = Hasher.HashPassword(user.getPassword(), challenge);
            String clientResponse = communicator.receive();
            String OTP = communicator.receive();

            if(LoginHandler.testUserCreditials(user,response,clientResponse,OTP)){
                communicator.send("Authenticated");
                System.out.println("Authenticated");
                return true;
            }
            else{
                communicator.send("Error");
            }
        }
    }

    static boolean testUserCreditials(User user, String response, String clientResponse, String OTP){
        System.out.println("OTP: " + getTOTPCode(user.getOtpSecret()));

        if(!response.equals(clientResponse))
            return false;

        if(!getTOTPCode(user.getOtpSecret()).equals(OTP))
            return false;

        return true;
    }

    private static String getTOTPCode(String secretKey) {
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(secretKey);
        String hexKey = Hex.encodeHexString(bytes);
        return TOTP.getOTP(hexKey);
    }

    public static User findUser(String username){
        List<User> users = ServerMain.databaseHandler.findUsers("personal_number", username);
        if(users.size() != 1) return null;
        return users.get(0);
    }

}
