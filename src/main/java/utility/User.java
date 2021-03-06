package utility;

import org.apache.commons.codec.binary.Base32;
import java.security.SecureRandom;

public class User {

    private int id;
    private final String name;
    private final String role;
    private final String personal_number;
    private final String password;
    private final String salt;
    private final String division;
    private final String otpSecret;

    public User(String name, String role, String personal_number, String password, String salt, String division,
                int id, String otpSecret) {
        this.name = name;
        this.role = role;
        this.personal_number = personal_number;
        this.password = password;
        this.salt = salt;
        this.division = division;
        this.id = id;
        this.otpSecret = otpSecret;
    }

    public User(String name, String role, String personal_number, String password, String salt, String division) {
        this.name = name;
        this.role = role;
        this.personal_number = personal_number;
        this.password = password;
        this.salt = salt;
        this.division = division;

        this.otpSecret = generateSecretKey(20);
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getPersonalNumber() {
        return personal_number;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public String getDivision() {
        return division;
    }

    public String getOtpSecret() {
        return otpSecret;
    }

    @Override
    public String toString() {
        if (division == null) {
            return "[" + id + "] " + name + "(" + personal_number + ") with the role " + role.toLowerCase()
                    + ". \nSalt: " + salt + "\nPassword: " + password;
        } else {
            return "[" + id + "] " + name + "(" + personal_number + ") with the role " + role.toLowerCase()
                    + ". works in divison: " + division + ". \nSalt: " + salt + "\nPassword: " + password
                    + "\n OTP secret: " + otpSecret;
        }
    }

    public static String generateSecretKey(Integer length) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes);
    }

}
