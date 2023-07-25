package Shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {

    String handleLoginRequest(String username, String password) throws RemoteException;

    String handleRegistrationRequest(String username, String password, String passwordVerification) throws RemoteException;

    String handleChangePasswordRequest(String username, String oldPassword, String newPassword, String newPasswordVerification) throws RemoteException;
}
