import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordHashing {
    public static String hashPassword(String password){
        return BCrypt.withDefaults().hashToString(10,password.toCharArray());
    }

    public static boolean verifyPassword(String password, String hashedPassword){
        return BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified;
    }
}
