package Server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The `UsernameValidator` class provides methods for validating username criteria.
 * It checks if a given username meets the length and character requirements.
 * The class also offers a method to retrieve the username criteria as a formatted string.
 *
 * <p> The maximum length requirement and regular expression pattern are defined as constants in the class.
 * The regular expression pattern ensures that the username contains only English letters and numbers.
 *
 * <p> This class is useful for enforcing username rules in an application.
 *
 * <p> This class does not require instances to be created, as all methods are static.
 *
 * <p> This class is co-authored by Aaron Barkan and Omer Bar.
 *
 * @author
 *   - Aaron Barkan
 *   - Omer Bar
 * @version 1.0
 * @since August 2023
 */
public class UsernameValidator {

    /**
     * The maximum allowed username length.
     */
    private static final long MAX_LENGTH = 128;

    /**
     * Validates if a given username meets the length and character criteria.
     * The username must have a length less than or equal to the maximum length
     * and consist of only English letters and numbers.
     *
     * @param username The username to be validated.
     * @return `true` if the username meets the criteria, `false` otherwise.
     */
    public static boolean validateUsername(String username) {
        boolean result = username.length() <= MAX_LENGTH;
        result &= usernameMatchesRegex(username);
        return result;
    }

    /**
     * Retrieves the username criteria as a formatted string.
     * The criteria specify the maximum length and character requirements.
     *
     * @return A formatted string specifying the username criteria.
     */
    public static String getUsernameCriteria() {
        return String.format("Username needs to be at most %d characters long,\n" +
                "and contain numbers and English letters only", MAX_LENGTH);
    }

    /**
     * Checks if a given username matches a regular expression pattern.
     * The pattern ensures that the username contains only English letters and numbers.
     *
     * @param username The username to be checked.
     * @return `true` if the username matches the pattern, `false` otherwise.
     */
    private static boolean usernameMatchesRegex(String username) {
        String regex = "^[a-zA-Z0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }
}
