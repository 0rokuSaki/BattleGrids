package Client;

import Client.Controllers.GameController;
import Shared.Client;
import Shared.GameScoreData;
import Shared.GameSession;
import Shared.Server;
import javafx.application.Platform;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * This class represents the client-side Model for the application. It facilitates communication
 * between the client and the server using RMI. It provides methods for user authentication, game management,
 * and handling game sessions. The class implements the {@link Shared.Client} interface to receive updates from the server.
 * <p>
 * This class manages the connection to the server, user authentication, game initialization, gameplay updates,
 * and other interactions between the client and the server.
 *
 * @author
 *   - Aaron Barkan
 *   - Omer Bar
 * @version 1.0
 * @since August, 2023
 */
public class ClientModel implements Client {

    //////////////////////////////////////////////////////
    ////////////////// STATIC VARIABLES //////////////////
    //////////////////////////////////////////////////////
    private static final String HOST = "localhost";
    private static final int PORT = 54321;
    private static final String CREDENTIALS_FILE_PATH = "./credentials";
    private static final ClientModel instance = new ClientModel();

    //////////////////////////////////////////////////////
    ///////////////// INSTANCE VARIABLES /////////////////
    //////////////////////////////////////////////////////
    private String username;
    private Server serverStub;
    private Client clientStub;
    private Registry serverRmiRegistry;
    private GameController gameController;

    //////////////////////////////////////////////////////
    /////////////////// STATIC METHODS ///////////////////
    //////////////////////////////////////////////////////
    /**
     * Returns the singleton instance of the ClientModel class.
     *
     * @return The singleton instance of ClientModel.
     */
    public static ClientModel getInstance() {
        return instance;
    }

    /**
     * Loads saved credentials from a file.
     *
     * @return The loaded Credentials, or null if loading fails.
     */
    public static Credentials loadCredentials() {
        try (FileInputStream fileIn = new FileInputStream(CREDENTIALS_FILE_PATH);
             ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {

            return  (Credentials) (objectIn.readObject());
        }
        catch (IOException | ClassNotFoundException ignored) {}
        return null;
    }

    /**
     * Saves user credentials to a file.
     *
     * @param username The username to save.
     * @param password The password to save.
     */
    public static void saveCredentials(final String username, final String password) {
        try (FileOutputStream fileOut = new FileOutputStream(CREDENTIALS_FILE_PATH);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

            objectOut.writeObject(new Credentials(username, password));
        } catch (IOException ignored) {}
    }

    /**
     * Deletes the saved credentials file.
     *
     * @return true if the file was deleted successfully, false otherwise.
     */
    public static boolean deleteCredentials() {
        File credentialsFile = new File(CREDENTIALS_FILE_PATH);
        return credentialsFile.delete();
    }

    //////////////////////////////////////////////////////
    /////////////////// REMOTE METHODS ///////////////////
    //////////////////////////////////////////////////////
    @Override
    public void testConnection() throws RemoteException {}

    @Override
    public void initializeGame(GameSession gameSession) throws RemoteException {
        Platform.runLater(() -> {
            if (gameController != null) {
                gameController.initializeGame(gameSession);
            }
        });
    }

    @Override
    public void updateGame(GameSession gameSession) throws RemoteException {
        Platform.runLater(() -> {
            if (gameController != null) {
                gameController.updateGame(gameSession);
            }
        });
    }

    @Override
    public void terminateGame(String message) throws RemoteException {
        Platform.runLater(() -> {
            if (gameController != null) {
                gameController.terminateGame(message);
            }
        });
    }

    //////////////////////////////////////////////////////
    /////////////////// PUBLIC METHODS ///////////////////
    //////////////////////////////////////////////////////
    /**
     * Sets the GameController instance that will handle game-related interactions on the client side.
     *
     * @param gameController The GameController instance.
     */
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Initializes the client-side connection to the server. This method attempts to establish
     * an RMI connection to the server and export the client object for remote method invocation.
     *
     * @return true if initialization is successful, false otherwise.
     */
    public boolean initialize() {
        try {
            serverRmiRegistry = LocateRegistry.getRegistry(HOST, PORT);         // Locate server's registry
            serverStub = (Server) serverRmiRegistry.lookup("GameServer"); // Obtain a reference to GameServer
            if (clientStub == null) {
                clientStub = (Client) UnicastRemoteObject.exportObject(this, 0); // Export self
            }
            return true;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Finalizes the client model by logging out from the server and un-exporting the client object.
     */
    public void finalize() {
        logOut(); // Log out from server
        try {
            UnicastRemoteObject.unexportObject(this, true);  // Un-export self
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the current username of the logged-in user.
     *
     * @return The username of the logged-in user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Attempts to log in a user with the provided username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return A message indicating the result of the login attempt.
     */
    public String logIn(String username, String password) {
        String returnMessage;
        try {
            returnMessage = serverStub.handleLoginRequest(username, password);
            if (returnMessage.equals("")) {
                serverRmiRegistry.rebind(username, clientStub); // Rebind to server's registry
                this.username = username;                       // Assign username
            }
        } catch (NullPointerException | RemoteException ignored) {
            returnMessage = "Cannot reach server";
        }
        return returnMessage;
    }

    /**
     * Logs out the currently logged-in user from the server.
     */
    public void logOut() {
        try {
            if (serverStub != null && username != null) {
                serverStub.handleLogoutRequest(username);
            }
            if (serverRmiRegistry != null && username != null) {
                serverRmiRegistry.unbind(username);
            }
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        username = null;
    }

    /**
     * Attempts to register a new user with the provided username, password, and password verification.
     *
     * @param username              The desired username.
     * @param password              The desired password.
     * @param passwordVerification The repeated password for verification.
     * @return A message indicating the result of the registration attempt.
     */
    public String register(String username, String password, String passwordVerification) {
        String returnMessage;
        try {
            returnMessage = serverStub.handleRegistrationRequest(username, password, passwordVerification);
            if (returnMessage.equals("")) {
                serverRmiRegistry.rebind(username, clientStub); // Rebind to server's registry
                this.username = username;                       // Assign username
            }
        } catch (NullPointerException | RemoteException ignored) {
            returnMessage = "Cannot reach server";
        }
        return returnMessage;
    }

    /**
     * Attempts to change the password of the currently logged-in user.
     *
     * @param oldPassword             The user's current password.
     * @param newPassword             The desired new password.
     * @param newPasswordVerification The repeated new password for verification.
     * @return A message indicating the result of the password change attempt.
     */
    public String changePassword(String oldPassword, String newPassword, String newPasswordVerification) {
        try {
            return serverStub.handleChangePasswordRequest(username, oldPassword, newPassword, newPasswordVerification);
        } catch (NullPointerException | RemoteException ignored) {
            return "Cannot reach server";
        }
    }

    /**
     * Retrieves the list of available games from the server.
     *
     * @return A list of available game names.
     */
    public ArrayList<String> getGamesList() {
        try {
            return serverStub.handleGetGamesListRequest();
        } catch (RemoteException ignored) {
            return null;
        }
    }

    /**
     * Requests to play a game with the specified name.
     *
     * @param gameName The name of the game to play.
     * @return A message indicating the result of the play game request.
     */
    public String playGame(String gameName) {
        try {
            return serverStub.handlePlayGameRequest(username, gameName);
        } catch (RemoteException ignored) {
            return "Cannot reach server";
        }
    }

    /**
     * Sends a move to the server for processing in the specified game session.
     *
     * @param sessionNumber The session number of the game.
     * @param row           The row where the move is made.
     * @param col           The column where the move is made.
     * @return A message indicating the result of the move request.
     */
    public String makeMove(long sessionNumber, int row, int col) {
        try {
            return serverStub.handleMakeMoveRequest(sessionNumber, row, col);
        } catch (RemoteException e) {
            e.printStackTrace();
            return "Cannot reach server";
        }
    }

    /**
     * Requests to quit the game with the specified session number.
     *
     * @param sessionNumber The session number of the game.
     * @return A message indicating the result of the quit game request.
     */
    public String quitGame(long sessionNumber) {
        try {
            return serverStub.handleQuitGameRequest(username, sessionNumber);
        } catch (RemoteException e) {
            e.printStackTrace();
            return "Cannot reach server";
        }
    }

    /**
     * Requests to quit the game with the specified name.
     *
     * @param gameName The name of the game to quit.
     * @return A message indicating the result of the quit game request.
     */
    public String quitGame(String gameName) {
        try {
            return serverStub.handleQuitGameRequest(username, gameName);
        } catch (RemoteException e) {
            e.printStackTrace();
            return "Cannot reach server";
        }
    }

    /**
     * Retrieves the list of game score data for the specified game.
     *
     * @param gameName The name of the game.
     * @return A list of game score data.
     */
    public ArrayList<GameScoreData> getScoreList(String gameName) {
        try {
            return serverStub.handleGetScoreListRequest(gameName);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    //////////////////////////////////////////////////////
    /////////////////// PRIVATE METHODS //////////////////
    //////////////////////////////////////////////////////

    /**
     * Private constructor to prevent external instantiation.
     * Initializes instance variables to default values.
     */
    private ClientModel() {
        username = null;
        serverStub = null;
        clientStub = null;
        serverRmiRegistry = null;
    }
}
