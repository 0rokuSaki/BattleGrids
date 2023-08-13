package Shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Server extends Remote {

    String handleLoginRequest(String username, String password) throws RemoteException;

    void handleLogoutRequest(String username) throws RemoteException;

    String handleRegistrationRequest(String username, String password, String passwordVerification) throws RemoteException;

    String handleChangePasswordRequest(String username, String oldPassword, String newPassword, String newPasswordVerification) throws RemoteException;

    ArrayList<String> handleGetGamesListRequest() throws RemoteException;

    String handlePlayGameRequest(String username, String gameName) throws RemoteException;

    String handleMakeMoveRequest(long sessionNumber, int row, int col) throws RemoteException;

    String handleQuitGameRequest(long sessionNumber) throws RemoteException;

    String handleQuitGameRequest(String username, String gameName) throws RemoteException;
}
