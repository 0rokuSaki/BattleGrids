package Server;

import Shared.Client;
import Shared.GameSession;
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
import java.util.concurrent.ConcurrentHashMap;

public class ServerImpl implements Server, Runnable {

    private final DBManager dbManager;

    private final Collection<String> loggedInUsersPool;

    private final HashMap<String, Queue<String>> gameWaitingQueues;

    private final ArrayList<String> gamesList;

    private final ConcurrentHashMap<Long, GameSession> gameSessions;

    private final Registry rmiRegistry;

    public ServerImpl(Registry registry) throws ClassNotFoundException, SQLException {
        rmiRegistry = registry;
        dbManager = new DBManagerImpl();
        loggedInUsersPool = Collections.synchronizedCollection(new ArrayList<>());
        gamesList = new ArrayList<>(Arrays.asList("Connect Four", "Tic Tac Toe"));
        gameSessions = new ConcurrentHashMap<>();
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
        // Get a user from the waiting queue and make sure he is logged in
        while (!loggedInUsersPool.contains(otherUser)) {
            loggedInUsersPool.remove(otherUser);
            otherUser = waitingQueue.poll();
            if (otherUser == null) {
                waitingQueue.add(username);
                return "";
            }
        }

        // Get remote references for the users
        Client player1;
        Client player2;
        try {
            player1 = (Client) rmiRegistry.lookup(otherUser);
            player2 = (Client) rmiRegistry.lookup(username);
        } catch (NotBoundException e) {
            e.printStackTrace();
            return "Internal server error";
        }

        // Create a game session object
        GameSession gameSession;
        switch (gameName) {
            case "Connect Four":
                gameSession = new ConnectFourGameSession(otherUser, username);
                break;
            default:
                return "Internal server error";
        }
        gameSessions.put(gameSession.getSessionNumber(), gameSession);

        // Notify clients
        player1.initializeGame(gameSession);
        player2.initializeGame(gameSession);

        return "";
    }

    @Override
    public String handleMakeMoveRequest(long sessionNumber, int row, int col) throws RemoteException {
        GameSession gameSession = gameSessions.get(sessionNumber);
        if (gameSession == null) {
            return "Invalid session number";
        }
        gameSession.makeMove(row, col);  // Make the move
        String winner = gameSession.getWinner();
        if (winner != null) {
            gameSessions.remove(sessionNumber);  // Remove session if game ended
            gameSession.releaseNumber();               // Release session
        }

        // Get remote references for the users
        Client player1;
        Client player2;
        try {
            player1 = (Client) rmiRegistry.lookup(gameSession.getPlayer1());
            player2 = (Client) rmiRegistry.lookup(gameSession.getPlayer2());
        } catch (NotBoundException e) {
            e.printStackTrace();
            return "Internal server error";
        }

        // Notify clients
        player1.updateGame(gameSession);
        player2.updateGame(gameSession);

        return "";
    }

    @Override
    public String handleQuitGameRequest(long sessionNumber) throws RemoteException {
        GameSession gameSession = gameSessions.get(sessionNumber);
        if (gameSession == null) {
            return "Invalid session number";
        }
        gameSessions.remove(sessionNumber);  // Remove session from gameSessions
        gameSession.releaseNumber();         // Release session number
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
        new Thread(server).start();                    // Start server thread

        Server stub = (Server) UnicastRemoteObject.exportObject(server, 0);  // Export object
        LocateRegistry.getRegistry(PORT).bind("GameServer", stub);          // Bind stub
        System.out.println("Server is ready");
    }
}
