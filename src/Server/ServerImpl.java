package Server;

import Shared.Client;
import Shared.Server;

import javax.xml.bind.DatatypeConverter;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.*;

public class ServerImpl implements Server, Runnable {

    private final DBManager dbManager;

    private final Collection<String> loggedInUsersPool;

    private final HashMap<String, Queue<String>> gameWaitingQueues;

    private final ArrayList<String> gamesList;

    private final Registry rmiRegistry;

    public ServerImpl(Registry registry) throws ClassNotFoundException, SQLException {
        rmiRegistry = registry;
        dbManager = new DBManagerImpl();
        loggedInUsersPool = Collections.synchronizedCollection(new ArrayList<>());
        gamesList = new ArrayList<>(Arrays.asList("Tic-Tac-Toe", "Connect Four"));
        gameWaitingQueues = new HashMap<>();
        for (String game : gamesList) {
            gameWaitingQueues.put(game, new LinkedList<>());
        }
    }

    @Override
    public String handleLoginRequest(String username, String password) throws RemoteException {
        if (loggedInUsersPool.contains(username)) {
            return "User is already logged in";
        }
        String passwordHash = dbManager.getPasswordHash(username);
        if (passwordHash == null || !passwordHash.equals(getMd5DigestString(password))) {
            return "Incorrect username and/or password";
        }
        loggedInUsersPool.add(username);
        System.out.println("User " + username + " logged in");
        return "";
    }

    @Override
    public void handleLogoutRequest(String username) throws RemoteException {
        if (loggedInUsersPool.remove(username)) {
            System.out.println("User " + username + " logged out");
        }
    }

    @Override
    public String handleRegistrationRequest(String username, String password, String passwordVerification) throws RemoteException {
        if (dbManager.userExists(username) == null) {
            return "Internal server error";
        }
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
        if (dbManager.addUser(username, getMd5DigestString(password)) == null || !loggedInUsersPool.add(username)) {
            return "Internal server error";
        }
        System.out.println("User " + username + " registered");
        return "";
    }

    @Override
    public String handleChangePasswordRequest(String username, String oldPassword, String newPassword, String newPasswordVerification) throws RemoteException {
        if (!loggedInUsersPool.contains(username)) {
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
        if (!loggedInUsersPool.contains(username)) {
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

            // Remove all disconnected users from loggedInUsersPool
            Iterator<String> iter = loggedInUsersPool.iterator();
            while (iter.hasNext()) {
                String username = iter.next();
                try {
                    Client remote = (Client) rmiRegistry.lookup(username);
                    remote.testConnection();
                } catch (RemoteException | NotBoundException ignored) {
                    System.out.println("User " + username + " is disconnected. Logging him out...");
                    iter.remove();
                }
            }

            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws RemoteException, AlreadyBoundException, ClassNotFoundException, SQLException {
        final int PORT = 54321;

        Registry registry = LocateRegistry.createRegistry(PORT);   // Create RMI Registry

        ServerImpl server = new ServerImpl(registry);  // Instantiate GameServer object
        new Thread(server).start();            // Start server thread

        Server stub =
                (Server) UnicastRemoteObject.exportObject((Server) server, 0);  // Export object
        LocateRegistry.getRegistry(PORT).bind("GameServer", stub);   // Bind stub
        System.out.println("Server is ready");
    }
}
