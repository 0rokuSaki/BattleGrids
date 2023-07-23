package Shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {
    boolean handleLoginRequest(String username, String passwordHash) throws RemoteException;
}
