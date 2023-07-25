package Server;

import Shared.Server;

import javax.xml.bind.DatatypeConverter;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerImpl implements Server, Runnable {

    private final DBManagerDummy dbManager = new DBManagerDummy();

    private final ConcurrentHashMap<String, Integer> connectedUsersPool = new ConcurrentHashMap<>();

    @Override
    public void probe() throws RemoteException {}

    @Override
    public boolean handleKeepAlive(String username) throws RemoteException {
        if (connectedUsersPool.get(username) != null) {
            connectedUsersPool.replace(username, 1);
            return true;
        }
        return false;
    }

    @Override
    public String handleLoginRequest(String username, String password) throws RemoteException {
        String passwordHash = dbManager.getPasswordHash(username);
        if (passwordHash == null || !passwordHash.equals(getMd5DigestString(password))) {
            return "Incorrect username and/or password";
        }
        connectedUsersPool.put(username, 1);
        System.out.println("User " + username + " logged in");
        return "";
    }

    @Override
    public void handleLogoutRequest(String username) throws RemoteException {
        if (connectedUsersPool.get(username) != null) {
            connectedUsersPool.remove(username);
            System.out.println("User " + username + " logged out");
        }
    }

    @Override
    public String handleRegistrationRequest(String username, String password, String passwordVerification) throws RemoteException {
        if (dbManager.userExists(username)) {
            return "Username already exists";
        }
        if (username.equals("")) {
            return "Invalid username";
        }
        if (!password.equals(passwordVerification)) {
            return "Passwords do not match";
        }
        if (!PasswordValidator.validatePassword(password)) {
            return PasswordValidator.getPasswordCriteria();
        }
        dbManager.setUser(username, getMd5DigestString(password));
        connectedUsersPool.put(username, 1);
        System.out.println("User " + username + " registered");
        return "";
    }

    @Override
    public String handleChangePasswordRequest(String username, String oldPassword, String newPassword, String newPasswordVerification) throws RemoteException {
        if (connectedUsersPool.get(username) == null) {
            return "Unable to identify user";
        }
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
        System.out.println("User " + username + " changed password");
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

    @Override
    public void run() {
        while (true) {
            for (Iterator<Map.Entry<String, Integer>> it = connectedUsersPool.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Integer> entry = it.next();

                if (entry.getValue() == 1) {  // Update connected users pool
                    entry.setValue(0);
                } else {  // entry.getValue() is 0
                    it.remove();  //  Remove disconnected user from pool
                }
            }

            // Sleep for some time
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        final int PORT = 54321;

        ServerImpl server = new ServerImpl();  // Instantiate GameServer object
        new Thread(server).start();            // Start server thread

        LocateRegistry.createRegistry(PORT);   // Create RMI Registry
        Server stub =
                (Server) UnicastRemoteObject.exportObject((Server) server, 0);  // Export object
        LocateRegistry.getRegistry(PORT).bind("GameServer", stub);   // Bind stub
        System.out.println("Server is ready");
    }
}
