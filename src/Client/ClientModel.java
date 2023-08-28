/**
 * ClientModel.java
 *
 * This class represents the client side Model for the application.
 *
 * @author Aaron Barkan, Omer Bar
 * @version 1.0
 * @since August, 2023
 */

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
    public static ClientModel getInstance() {
        return instance;
    }

    /**
     * Reads a Credentials object from a file.
     * @return Credentials object on successful read, null otherwise.
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
     * Writes credentials to a file (in a Credentials object).
     * @param username Username.
     * @param password Password.
     */
    public static void saveCredentials(final String username, final String password) {
        try (FileOutputStream fileOut = new FileOutputStream(CREDENTIALS_FILE_PATH);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

            objectOut.writeObject(new Credentials(username, password));
        } catch (IOException ignored) {}
    }

    /**
     * Deletes credentials file.
     * @return True if delete was successful, False otherwise.
     */
    public static boolean deleteCredentials() {
        File credentialsFile = new File(CREDENTIALS_FILE_PATH);
        return credentialsFile.delete();
    }

    //////////////////////////////////////////////////////
    /////////////////// REMOTE METHODS ///////////////////
    //////////////////////////////////////////////////////

    /**
     * Remote method for the server to test RMI connectivity.
     * @throws RemoteException If RMI exception occurred.
     */
    @Override
    public void testConnection() throws RemoteException {}

    /**
     * Remote method for the server to trigger game initialization.
     * @param gameSession Game Session object.
     * @throws RemoteException If RMI exception occurred.
     */
    @Override
    public void initializeGame(GameSession gameSession) throws RemoteException {
        Platform.runLater(() -> {
            if (gameController != null) {
                gameController.initializeGame(gameSession);
            }
        });
    }

    /**
     * Remote method for the server to trigger game update.
     * @param gameSession Game Session object.
     * @throws RemoteException If RMI exception occurred.
     */
    @Override
    public void updateGame(GameSession gameSession) throws RemoteException {
        Platform.runLater(() -> {
            if (gameController != null) {
                gameController.updateGame(gameSession);
            }
        });
    }

    /**
     * Remote method for the server to trigger game termination.
     * @throws RemoteException If RMI exception occurred.
     */
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
     * Sets a reference to the game controller. Used for updating the game by the server.
     * @param gameController Reference to game controller.
     */
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Connects to the RMI registry and gets a remote reference to the server.
     * @return True if initialization is successful, false otherwise.
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
     * Performs housekeeping (such as logging out from the server) before destroying the object.
     */
    public void finalize() {
        logOut(); // Log out from server
        try {
            UnicastRemoteObject.unexportObject(this, true);  // Un-export self
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    /**
     * Performs a login request to the server.
     * @param username Username.
     * @param password Password.
     * @return Empty string if successful, error message otherwise.
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
     * Performs a logout request to the server.
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
     * Performs a registration request to the server.
     * @param username Username.
     * @param password Password.
     * @param passwordVerification Password verification.
     * @return Empty string if successful, error message otherwise.
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
     * Performs a change password request to the server.
     * @param oldPassword Old password.
     * @param newPassword New password.
     * @param newPasswordVerification New password verification.
     * @return Empty string if successful, error message otherwise.
     */
    public String changePassword(String oldPassword, String newPassword, String newPasswordVerification) {
        try {
            return serverStub.handleChangePasswordRequest(username, oldPassword, newPassword, newPasswordVerification);
        } catch (NullPointerException | RemoteException ignored) {
            return "Cannot reach server";
        }
    }

    /**
     * Gets the games list from the server.
     * @return A list of the games or null.
     */
    public ArrayList<String> getGamesList() {
        try {
            return serverStub.handleGetGamesListRequest();
        } catch (RemoteException ignored) {
            return null;
        }
    }

    /**
     * Performs a play-game request to server.
     * @param gameName Game name.
     * @return Empty string if successful, error message otherwise.
     */
    public String playGame(String gameName) {
        try {
            return serverStub.handlePlayGameRequest(username, gameName);
        } catch (RemoteException ignored) {
            return "Cannot reach server";
        }
    }

    /**
     * Performs a make-move request to server.
     * @param sessionNumber Session number.
     * @param row Move row.
     * @param col Move column.
     * @return Empty string if successful, error message otherwise.
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
     * Requests the server to quit the game.
     * @param sessionNumber Session number.
     * @return Empty string if successful, error message otherwise.
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
     * Requests the server to quit the game.
     * @param gameName Game Name.
     * @return Empty string if successful, error message otherwise.
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
     * Gets the game score data for a given game.
     * @param gameName Game name.
     * @return A list of the game scores, null otherwise.
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
     * Constructor.
     */
    private ClientModel() {
        username = null;
        serverStub = null;
        clientStub = null;
        serverRmiRegistry = null;
    }
}
