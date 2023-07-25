package Shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {

    String handleLoginRequest(String username, String passwordHash) throws RemoteException;

    String handleRegistrationRequest(String username, String passwordHash) throws RemoteException;

    String handleChangePasswordRequest(String username, String oldPasswordHash, String newPasswordHash) throws RemoteException;
}
