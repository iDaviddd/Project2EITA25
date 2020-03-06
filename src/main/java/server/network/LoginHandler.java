package server.network;

import de.taimos.totp.TOTP;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import server.ServerMain;
import utility.Communicator;
import utility.Hasher;
import utility.Request;
import utility.User;

import java.util.List;

class LoginHandler {

    static User login(Communicator communicator) {
        Request username = communicator.receive();
        if (!username.type.equals("username")) {
            System.out.println("Login did not receive username");
            communicator.send(new Request("authentication", "post", "false"));
            return null;
        }

        User user = findUser(username.data);
        if (user == null) {
            System.out.println("User not found");
            communicator.send(new Request("authentication", "post", "false"));
            return null;
        }

        String challenge = User.generateSecretKey(32);

        communicator.send(new Request("salt", "post", user.getSalt()));
        communicator.send(new Request("challenge", "post", challenge));

        String response = Hasher.HashPassword(user.getPassword(), challenge);

        Request clientResponse = communicator.receive();
        if (!clientResponse.type.equals("challenge response")) {
            System.out.println("Login did not receive challenge response");
            communicator.send(new Request("authentication", "post", "false"));
            return null;
        }

        Request OTP = communicator.receive();
        if (!OTP.type.equals("OTP")) {
            System.out.println("Login did not receive OTP");
            communicator.send(new Request("authentication", "post", "false"));
            return null;
        }

        if (LoginHandler.testUserCreditials(user, response, clientResponse.data, OTP.data)) {
            communicator.send(new Request("authentication", "post", "true"));
            communicator.send(new Request("role", "post", user.getRole()));
            System.out.println("Authenticated");
            return user;
        } else {
            communicator.send(new Request("authentication", "post", "false"));
            return null;
        }
    }

    private static boolean testUserCreditials(User user, String response, String clientResponse, String OTP) {
        System.out.println("OTP: " + getTOTPCode(user.getOtpSecret()));

        if (!response.equals(clientResponse))
            return false;

        return getTOTPCode(user.getOtpSecret()).equals(OTP);
    }

    private static String getTOTPCode(String secretKey) {
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(secretKey);
        String hexKey = Hex.encodeHexString(bytes);
        return TOTP.getOTP(hexKey);
    }

    private static User findUser(String username) {
        List<User> users = ServerMain.databaseHandler.findUsers("personal_number", username);
        if (users.size() != 1) return null;
        return users.get(0);
    }

}
