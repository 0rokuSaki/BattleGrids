package Server;

/**
 * The `PasswordValidator` class provides methods for validating password criteria.
 * It checks if a given password meets the minimum and maximum length requirements.
 * The class also offers a method to retrieve the password criteria as a formatted string.
 *
 * <p> The minimum and maximum length requirements are defined as constants in the class.
 *
 * <p> This class is useful for enforcing password security rules in an application.
 *
 * <p> This class does not require instances to be created, as all methods are static.
 *
 * @author
 *   - Aaron Barkan
 *   - Omer Bar
 * @version 1.0
 * @since August 2023
 */
public class PasswordValidator {
    /**
     * The minimum allowed password length.
     */
    private static final long MIN_LENGTH = 8;

    /**
     * The maximum allowed password length.
     */
    private static final long MAX_LENGTH = 16;

    /**
     * Validates if a given password meets the length criteria.
     * The password must have a length greater than or equal to the minimum length
     * and less than or equal to the maximum length.
     *
     * @param password The password to be validated.
     * @return `true` if the password meets the length criteria, `false` otherwise.
     */
    public static boolean validatePassword(String password) {
        long passwordLength = password.length();
        return MIN_LENGTH <= passwordLength && passwordLength <= MAX_LENGTH;
    }

    /**
     * Retrieves the password criteria as a formatted string.
     * The criteria specify the range of allowed password lengths.
     *
     * @return A formatted string specifying the password criteria.
     */
    public static String getPasswordCriteria() {
        return String.format("Password needs to be %d - %d characters long", MIN_LENGTH, MAX_LENGTH);
    }
}
