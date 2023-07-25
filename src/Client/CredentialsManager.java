package Client;

import java.io.*;

public class CredentialsManager {
    private String credentialsFilePath;

    public CredentialsManager(String credentialsFilePath) {
        this.credentialsFilePath = credentialsFilePath;
    }

    public String getCredentialsFilePath() {
        return credentialsFilePath;
    }

    public void setCredentialsFilePath(String credentialsFilePath) {
        this.credentialsFilePath = credentialsFilePath;
    }

    public Credentials loadCredentialsFromFile() {
        try (FileInputStream fileIn = new FileInputStream(credentialsFilePath);
             ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {

            return  (Credentials) (objectIn.readObject());
        }
        catch (IOException | ClassNotFoundException ignored) {}
        return null;
    }

    public void saveCredentialsToFile(String username, String passwordHash) {
        try (FileOutputStream fileOut = new FileOutputStream(credentialsFilePath);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

            objectOut.writeObject(new Credentials(username, passwordHash));
        } catch (IOException ignored) {}
    }

    public boolean deleteCredentialsFile() {
        File credentialsFile = new File(credentialsFilePath);
        return credentialsFile.delete();
    }
}
