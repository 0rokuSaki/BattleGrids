package Shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {
    void testConnection() throws RemoteException;

    void initializeGame(GameSession gameSession) throws RemoteException;

    void updateGame(GameSession gameSession) throws RemoteException;

    void terminateGame(String message) throws RemoteException;
}
