package Server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsernameValidator {

    private static final long MAX_LENGTH = 128;

    public static boolean validateUsername(String username) {
        boolean result = username.length() <= MAX_LENGTH;
        result &= usernameMatchesRegex(username);
        return result;
    }

    public static String getUsernameCriteria() {
        return String.format("Username needs to be at most %d characters long,\n" +
                "and contain numbers and English letters only", MAX_LENGTH);
    }

    private static boolean usernameMatchesRegex(String username) {
        String regex = "^[a-zA-Z0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }
}
