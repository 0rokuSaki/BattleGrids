package Server;

import java.io.*;
import java.util.Dictionary;
import java.util.Hashtable;

public class DBManagerDummy {
    private Dictionary<String, String> usersTable = null;

    private String usersTablePath = "./usersTable";

    public DBManagerDummy() {
        loadUsersTable();
        if (usersTable == null) {
            usersTable = new Hashtable<>();
            saveUsersTable();
        }
    }

    public String getPasswordHash(String username) {
        return usersTable.get(username);
    }

    public boolean userExists(String username) {
        return usersTable.get(username) != null;
    }

    public void setUser(String username, String passwordHash) {
        usersTable.put(username, passwordHash);
        saveUsersTable();
    }

    public void setPasswordHash(String username, String passwordHash) {
        usersTable.put(username, passwordHash);
        saveUsersTable();
    }

    private void loadUsersTable() {
        try (FileInputStream fileIn = new FileInputStream(usersTablePath);
             ObjectInputStream objIn = new ObjectInputStream(fileIn)) {
            usersTable = (Dictionary<String, String>) (objIn.readObject());
        } catch (IOException | ClassNotFoundException ignored) {}
    }

    private void saveUsersTable() {
        try (FileOutputStream fileOut = new FileOutputStream(usersTablePath);
             ObjectOutputStream objOut = new ObjectOutputStream(fileOut)) {
            objOut.writeObject(usersTable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
