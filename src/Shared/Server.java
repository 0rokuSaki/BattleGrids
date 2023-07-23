package Shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {
    public enum ReturnCode {
        NO_ERROR,
        USER_ALREADY_EXISTS,
        INCORRECT_LOGIN_INFO,
        GENERAL_ERROR
    }

    ReturnCode handleLoginRequest(String username, String passwordHash) throws RemoteException;

    ReturnCode handleRegistrationRequest(String username, String passwordHash) throws RemoteException;
}
