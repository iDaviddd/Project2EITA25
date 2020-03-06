package utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {

    public static String HashPassword(String password, String salt) {

        String saltedPassword = salt + password;

        StringBuilder hash = new StringBuilder();
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-512");
            byte[] hashedBytes = sha.digest(saltedPassword.getBytes());
            char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f'};
            for (byte b : hashedBytes) {
                hash.append(digits[(b & 0xf0) >> 4]);
                hash.append(digits[b & 0x0f]);
            }
        } catch (NoSuchAlgorithmException e) {
            // handle error here.
        }
        return hash.toString();
    }
}
