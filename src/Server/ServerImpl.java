package Server;

import Server.GameSession.ConnectFourGameSession;
import Server.GameSession.TicTacToeGameSession;
import Shared.Client;
import Server.GameSession.GameSessionBase;
import Shared.Server;

import javax.xml.bind.DatatypeConverter;
import java.rmi.AccessException;
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
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

public class ServerImpl implements Server, Runnable {

    private final Logger logger;

    private final DBManager dbManager;

    private final Collection<String> loggedInUsersPool;

    private final HashMap<String, Queue<String>> gameWaitingQueues;

    private final ArrayList<String> gamesList;

    private final ConcurrentHashMap<Long, GameSessionBase> gameSessions;

    private final Registry rmiRegistry;

    public ServerImpl(Registry registry) throws ClassNotFoundException, SQLException {
        logger = Logger.getLogger(ServerImpl.class.getName());
        rmiRegistry = registry;
        dbManager = new DBManagerImpl();
        loggedInUsersPool = Collections.synchronizedCollection(new ArrayList<>());
        gamesList = new ArrayList<>(Arrays.asList("Connect Four", "Tic Tac Toe"));
        gameSessions = new ConcurrentHashMap<>();
        gameWaitingQueues = new HashMap<>();
        for (String game : gamesList) {
            gameWaitingQueues.put(game, new ConcurrentLinkedQueue<>());
        }
    }

    @Override
    public String handleLoginRequest(String username, String password) throws RemoteException {
        if (loggedInUsersPool.contains(username)) {
            logger.info("Login request denied: user already logged in (username = '" + username + "')");
            return "User is already logged in";
        }
        String passwordHash = dbManager.getPasswordHash(username);
        if (passwordHash == null || !passwordHash.equals(getMd5DigestString(password))) {
            logger.info("Login request denied: incorrect username and/or password (username = '" + username + "')");
            return "Incorrect username and/or password";
        }
        loggedInUsersPool.add(username);
        logger.info("User " + username + " logged in");
        return "";
    }

    @Override
    public void handleLogoutRequest(String username) throws RemoteException {
        if (loggedInUsersPool.remove(username)) {
            logger.info("User " + username + " logged out");
        }
    }

    @Override
    public String handleRegistrationRequest(String username, String password, String passwordVerification) throws RemoteException {
        if (dbManager.userExists(username) == null) {
            logger.warning("Invalid return value from database");
            return "Internal server error";
        }
        if (dbManager.userExists(username)) {
            logger.info("Registration denied: username '" + username + "' already exists");
            return "Username already exists";
        }
        if (username.equals("")) {
            logger.info("Registration denied: invalid username: '" + username + "'");
            return "Invalid username";
        }
        if (!password.equals(passwordVerification)) {
            logger.info("Registration denied: passwords do not match (username = '" + username + "')");
            return "Passwords do not match";
        }
        if (!PasswordValidator.validatePassword(password)) {
            logger.info("Registration denied: password does not match criteria (username = '" + username + "')");
            return PasswordValidator.getPasswordCriteria();
        }
        if (dbManager.addUser(username, getMd5DigestString(password)) == null) {
            logger.warning("Registration denied: error inserting user to database");
            return "Internal server error";
        }
        if (!loggedInUsersPool.add(username)) {
            logger.warning("Registration denied: error inserting user to logged in users pool");
            return "Internal server error";
        }
        logger.info("User " + username + " registered");
        return "";
    }

    @Override
    public String handleChangePasswordRequest(String username, String oldPassword, String newPassword, String newPasswordVerification) throws RemoteException {
        if (!loggedInUsersPool.contains(username)) {
            logger.info("Change password request denied: logged in users pool does not contain user (username = '" + username +"')");
            return "Unable to identify user";
        }
        String passwordHash = dbManager.getPasswordHash(username);
        if (passwordHash == null) {
            logger.info("Change password request denied: user does not exist (username = '" + username + "' )");
            return "User does not exist";
        }
        if (!passwordHash.equals(getMd5DigestString(oldPassword))) {
            logger.info("Change password request denied: old password is incorrect (username = '" + username + "' )");
            return "Old password is incorrect";
        }
        if (!newPassword.equals(newPasswordVerification)) {
            logger.info("Change password request denied: passwords do not match (username = '" + username + "' )");
            return "Passwords do not match";
        }
        if (!PasswordValidator.validatePassword(newPassword)) {
            logger.info("Change password request denied: password does not match criteria (username = '" + username + "' )");
            return PasswordValidator.getPasswordCriteria();
        }
        if (dbManager.setPasswordHash(username, getMd5DigestString(newPassword)) == null) {
            logger.warning("Invalid return value from database");
            return "Internal server error";
        }
        logger.info("User " + username + " changed password");
        return "";
    }

    @Override
    public ArrayList<String> handleGetGamesListRequest() throws RemoteException {
        return gamesList;
    }

    @Override
    public String handlePlayGameRequest(String username, String gameName) throws RemoteException {
        if (!loggedInUsersPool.contains(username)) {
            logger.info("Play game request denied: logged in users pool does not contain user (username = '" + username +"')");
            return "Unable to identify user";
        }
        Queue<String> waitingQueue = gameWaitingQueues.get(gameName);
        if (waitingQueue == null) {
            logger.info("Play game request denied: invalid game name (username = ' " + username + "', game name = '" + gameName + "')");
            return "Invalid game selected";
        }
        String otherUser = waitingQueue.poll();
        if (otherUser == null) {
            logger.info("Added user '" + username + "' to game queue (game name = '" + gameName + "')");
            waitingQueue.add(username);
            return "";
        }

        // Make sure other user is logged in
        while (!loggedInUsersPool.contains(otherUser)) {
            otherUser = waitingQueue.poll();
            if (otherUser == null) {
                logger.info("Added user '" + username + "' to game queue (game name = '" + gameName + "')");
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
            logger.warning("Play game request denied: error during rmiRegistry.lookup");
            e.printStackTrace();
            return "Internal server error";
        }

        // Create a game session object
        GameSessionBase gameSession;
        switch (gameName) {
            case "Connect Four":
                gameSession = new ConnectFourGameSession(otherUser, username);
                break;
            case "Tic Tac Toe":
                gameSession = new TicTacToeGameSession(otherUser, username);
                break;
            default:
                logger.warning("Play game request denied: invalid game name (game name = '" + gameName + "')");
                return "Internal server error";
        }
        gameSessions.put(gameSession.getSessionNumber(), gameSession);

        // Notify clients
        try {
            player1.initializeGame(gameSession);
            player2.initializeGame(gameSession);
        } catch (RemoteException e) {
            gameSessions.remove(gameSession.getSessionNumber());
            logger.info("Play game request denied: one or more users is disconnected");
            return "Error while starting the game";
        }

        logger.info("Starting game: '" + gameName + "', session number: " + gameSession.getSessionNumber() + ", " +
                "Player 1: '" + gameSession.getPlayer1() + "', Player 2: '" + gameSession.getPlayer1() + "'");
        return "";
    }

    @Override
    public String handleMakeMoveRequest(long sessionNumber, int row, int col) throws RemoteException {
        GameSessionBase gameSession = gameSessions.get(sessionNumber);
        if (gameSession == null) {
            logger.info("Make move request denied: invalid session number (session number = " + sessionNumber + ")");
            return "Invalid session number";
        }
        gameSession.makeMove(row, col);  // Make the move

        String winner = gameSession.getWinner();
        if (winner != null) {
            gameSessions.remove(sessionNumber);  // Remove session if game ended
            gameSession.releaseNumber();         // Release session
        }

        // Get remote references for the users
        Client player1;
        Client player2;
        try {
            player1 = (Client) rmiRegistry.lookup(gameSession.getPlayer1());
            player2 = (Client) rmiRegistry.lookup(gameSession.getPlayer2());
        } catch (NotBoundException e) {
            logger.warning("Make move request denied: error during rmiRegistry.lookup");
            e.printStackTrace();
            return "Internal server error";
        }

        // Notify clients
        player1.updateGame(gameSession);
        player2.updateGame(gameSession);

        return "";
    }

    @Override
    public String handleQuitGameRequest(String username, long sessionNumber) throws RemoteException {
        GameSessionBase gameSession = gameSessions.get(sessionNumber);
        if (gameSession == null) {
            logger.info("Quit game request denied: invalid session number (username = '" + username + "'," +
                    "session number = " + sessionNumber + ")");
            return "Invalid session number";
        }
        gameSessions.remove(sessionNumber);  // Remove session from gameSessions
        gameSession.releaseNumber();         // Release session number
        gameSession.setPlayerQuit(username); // Set quitting player in session

        // Notify the winner
        Client winningPlayer;
        try {
            winningPlayer = (Client) rmiRegistry.lookup(gameSession.getWinner());
        } catch (NotBoundException e) {
            logger.warning("Quit game request denied: error during rmiRegistry.lookup");
            e.printStackTrace();
            return "Internal server error";
        }
        winningPlayer.updateGame(gameSession);
        logger.info("Player '" + username + "' quit the game");
        return "";
    }

    @Override
    public String handleQuitGameRequest(String username, String gameName) throws RemoteException {
        if (!loggedInUsersPool.contains(username)) {
            logger.info("Quit game request denied: unable to identify user (username = '" + username + "')");
            return "Unable to identify user";
        }
        Queue<String> waitingQueue = gameWaitingQueues.get(gameName);
        if (waitingQueue == null) {
            logger.info("Quit game request denied: invalid game name (username = '" + username + "'," +
                    "game name = '" + gameName + "')");
            return "Invalid game selected";
        }
        if (!waitingQueue.contains(username)) {
            logger.info("Quit game request denied: user not in waiting queue (username = '" + username + "'," +
                    "game name = '" + gameName + "')");
            return "Username is not in the waiting queue";
        }
        if (!waitingQueue.removeIf(element -> element.equals(username))) {
            logger.info("Quit game request denied: error removing user from waiting queue (username = '" + username + "'," +
                    "game name = '" + gameName + "')");
            return "Internal server error";
        }
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
            // Terminate games where at least one user disconnected
            Iterator<Map.Entry<Long, GameSessionBase>> gameSessionsIter = gameSessions.entrySet().iterator();
            while (gameSessionsIter.hasNext()) {
                Map.Entry<Long, GameSessionBase> entry = gameSessionsIter.next();

                // Test connection of each user
                Client client1 = null, client2 = null;
                try {
                    client1 = (Client) rmiRegistry.lookup(entry.getValue().getPlayer1());
                    client2 = (Client) rmiRegistry.lookup(entry.getValue().getPlayer2());
                    client1.testConnection();
                    client2.testConnection();
                    continue;  // Connection is OK, check next session
                } catch (AccessException e) {
                    e.printStackTrace();
                } catch (NotBoundException | RemoteException ignored) {}

                // Connection is not OK, terminate the game session
                logger.info("Terminating session " + entry.getKey() + " due to disconnected user");
                gameSessionsIter.remove();
                try {
                    if (client1 != null) {
                        client1.terminateGame("Game ended due to disconnected user");
                    }
                } catch (RemoteException ignored) {}
                try {
                    if (client2 != null) {
                        client2.terminateGame("Game ended due to disconnected user");
                    }
                } catch (RemoteException ignored) {}
            }

            // Remove all disconnected users from loggedInUsersPool
            Iterator<String> iter = loggedInUsersPool.iterator();
            while (iter.hasNext()) {
                String username = iter.next();
                try {
                    Client remote = (Client) rmiRegistry.lookup(username);
                    remote.testConnection();
                } catch (RemoteException | NotBoundException ignored) {
                    logger.info("User " + username + " is disconnected. Logging him out...");
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
