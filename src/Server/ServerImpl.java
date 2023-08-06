package Server;

import Shared.Server;

import javax.xml.bind.DatatypeConverter;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ServerImpl implements Server, Runnable {

    private final DBManager dbManager;

    private final ConcurrentHashMap<String, Integer> connectedUsersPool;

    private final HashMap<String, Queue<String>> gameWaitingQueues;

    private final ArrayList<String> gamesList;

    public ServerImpl() throws ClassNotFoundException, SQLException {
        dbManager = new DBManagerImpl();
        connectedUsersPool = new ConcurrentHashMap<>();
        gamesList = new ArrayList<>(Arrays.asList("Tic-Tac-Toe", "Connect Four"));
        gameWaitingQueues = new HashMap<>();
        for (String game : gamesList) {
            gameWaitingQueues.put(game, new LinkedList<>());
        }
    }

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
        if (connectedUsersPool.get(username) != null) {
            return "User is already logged in";
        }
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
        dbManager.addUser(username, getMd5DigestString(password));
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

    @Override
    public ArrayList<String> handleGetGamesListRequest() throws RemoteException {
        return gamesList;
    }

    @Override
    public synchronized String handlePlayGameRequest(String username, String gameName) throws RemoteException {
        if (connectedUsersPool.get(username) == null) {
            return "Unable to identify user";
        }
        Queue<String> waitingQueue = gameWaitingQueues.get(gameName);
        if (waitingQueue == null) {
            return "Invalid game selected";
        }
        if (waitingQueue.isEmpty()) {
            waitingQueue.add(username);
            return "";
        }
        String otherUser = waitingQueue.poll();
        if (otherUser == null) {
            return "Internal server error";
        }
        System.out.println("LET THE GAME BEGIN!");
        // TODO: Start game session
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

    public static void main(String[] args) throws RemoteException, AlreadyBoundException, ClassNotFoundException, SQLException {
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
