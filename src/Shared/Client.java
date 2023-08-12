package Shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {
    public void testConnection() throws RemoteException;

    public void initializeGame(GameSession gameSession) throws RemoteException;

    public void updateGame(GameSession gameSession) throws RemoteException;
}
