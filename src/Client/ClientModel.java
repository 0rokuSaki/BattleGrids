package Client;

import Client.Controllers.GameController;
import Shared.Client;
import Server.GameSession.GameSessionBase;
import Shared.GameScoreData;
import Shared.Server;
import javafx.application.Platform;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ClientModel implements Client {

    //////////////////////////////////////////////////////
    /////////////////// REMOTE METHODS ///////////////////
    //////////////////////////////////////////////////////
    @Override
    public void testConnection() throws RemoteException {}

    @Override
    public void initializeGame(GameSessionBase gameSession) throws RemoteException {
        Platform.runLater(() -> {
            if (gameController != null) {
                gameController.initializeGame(gameSession);
            }
        });
    }

    @Override
    public void updateGame(GameSessionBase gameSession) throws RemoteException {
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
    ////////////////// STATIC VARIABLES //////////////////
    //////////////////////////////////////////////////////
    private static final String HOST = "localhost";

    private static final int PORT = 54321;

    private static final ClientModel instance = new ClientModel();

    //////////////////////////////////////////////////////
    /////////////////// STATIC METHODS ///////////////////
    //////////////////////////////////////////////////////
    public static ClientModel getInstance() {
        return instance;
    }

    //////////////////////////////////////////////////////
    ///////////////// INSTANCE VARIABLES /////////////////
    //////////////////////////////////////////////////////
    private String username;

    private Server serverStub;

    private Client clientStub;

    private Registry serverRmiRegistry;

    private GameController gameController;

    //////////////////////////////////////////////////////
    /////////////////// PUBLIC METHODS ///////////////////
    //////////////////////////////////////////////////////
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

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

    public String changePassword(String oldPassword, String newPassword, String newPasswordVerification) {
        try {
            return serverStub.handleChangePasswordRequest(username, oldPassword, newPassword, newPasswordVerification);
        } catch (NullPointerException | RemoteException ignored) {
            return "Cannot reach server";
        }
    }

    public ArrayList<String> getGamesList() {
        try {
            return serverStub.handleGetGamesListRequest();
        } catch (RemoteException ignored) {
            return null;
        }
    }

    public String playGame(String gameName) {
        try {
            return serverStub.handlePlayGameRequest(username, gameName);
        } catch (RemoteException ignored) {
            return "Cannot reach server";
        }
    }

    public String makeMove(long sessionNumber, int row, int col) {
        try {
            return serverStub.handleMakeMoveRequest(sessionNumber, row, col);
        } catch (RemoteException e) {
            e.printStackTrace();
            return "Cannot reach server";
        }
    }

    public String quitGame(long sessionNumber) {
        try {
            return serverStub.handleQuitGameRequest(username, sessionNumber);
        } catch (RemoteException e) {
            e.printStackTrace();
            return "Cannot reach server";
        }
    }

    public String quitGame(String gameName) {
        try {
            return serverStub.handleQuitGameRequest(username, gameName);
        } catch (RemoteException e) {
            e.printStackTrace();
            return "Cannot reach server";
        }
    }

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
    private ClientModel() {
        username = null;
        serverStub = null;
        clientStub = null;
        serverRmiRegistry = null;
    }
}
