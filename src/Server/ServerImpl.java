package Server;

import Shared.Server;

import javax.xml.bind.DatatypeConverter;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ServerImpl implements Server {

    private final DBManagerDummy dbManager = new DBManagerDummy();

    @Override
    public String handleLoginRequest(String username, String password) throws RemoteException {
        String passwordHash = dbManager.getPasswordHash(username);
        if (passwordHash != null && passwordHash.equals(getMd5DigestString(password))) {
            return "";
        }
        return "Incorrect username and/or password";
    }

    @Override
    public String handleRegistrationRequest(String username, String password, String passwordVerification) throws RemoteException {
        if (dbManager.userExists(username)) {
            return "Username already exists";
        }
        if (!password.equals(passwordVerification)) {
            return "Passwords do not match";
        }
        if (!PasswordValidator.validatePassword(password)) {
            return PasswordValidator.getPasswordCriteria();
        }
        dbManager.setUser(username, getMd5DigestString(password));
        return "";
    }

    @Override
    public String handleChangePasswordRequest(String username, String oldPassword, String newPassword, String newPasswordVerification) throws RemoteException {
        String passwordHash = dbManager.getPasswordHash(username);
        if (passwordHash == null) {
            return "User does not exist";
        }
        if (!passwordHash.equals(getMd5DigestString(oldPassword))) {
            return "Old password is incorrect";
        }
        if (!newPassword.equals(newPasswordVerification)) {
            return "Passwords do not match";
        }
        if (!PasswordValidator.validatePassword(newPassword)) {
            return PasswordValidator.getPasswordCriteria();
        }
        dbManager.setPasswordHash(username, getMd5DigestString(newPassword));
        return "";
    }

    private static String getMd5DigestString(String inputString) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(inputString.getBytes());
            byte[] digest = md.digest();
            return DatatypeConverter.printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException ignored) {}
        return null;
    }

    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        final int PORT = 54321;
        LocateRegistry.createRegistry(PORT);       // Create RMI Registry
        Server server = new ServerImpl();  // Instantiate GameServer object
        Server stub =
                (Server) UnicastRemoteObject.exportObject(server, 0);  // Export object
        LocateRegistry.getRegistry(PORT).bind("GameServer", stub);   // Bind stub
        System.out.println("Server is ready");
    }
}
