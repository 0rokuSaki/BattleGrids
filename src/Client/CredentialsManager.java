package Client;

import java.io.*;

public class CredentialsManager {

    private static final String FILE_PATH = "./credentials";

    public static Credentials loadCredentials() {
        try (FileInputStream fileIn = new FileInputStream(FILE_PATH);
             ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {

            return  (Credentials) (objectIn.readObject());
        }
        catch (IOException | ClassNotFoundException ignored) {}
        return null;
    }

    public static void saveCredentials(final String username, final String password) {
        try (FileOutputStream fileOut = new FileOutputStream(FILE_PATH);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

            objectOut.writeObject(new Credentials(username, password));
        } catch (IOException ignored) {}
    }

    public static boolean deleteCredentials() {
        File credentialsFile = new File(FILE_PATH);
        return credentialsFile.delete();
    }
}
