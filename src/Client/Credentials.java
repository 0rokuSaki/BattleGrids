/**
 * Credentials.java
 *
 * This class represents a username and password pair to be saved on a file.
 *
 * @author Aaron Barkan, Omer Bar
 * @version 1.0
 * @since August, 2023
 */

package Client;

import java.io.Serializable;

public class Credentials implements Serializable {

    private String username;

    private String password;

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
