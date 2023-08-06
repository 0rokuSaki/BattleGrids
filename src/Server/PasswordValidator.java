package Server;

public class PasswordValidator {
    private static final long MIN_LENGTH = 8;

    private static final long MAX_LENGTH = 16;

    public static boolean validatePassword(String password) {
        long passwordLength = password.length();
        return MIN_LENGTH <= passwordLength && passwordLength <= MAX_LENGTH;
    }

    public static String getPasswordCriteria() {
        return String.format("Password needs to be %d - %d characters long", MIN_LENGTH, MAX_LENGTH);
    }
}
