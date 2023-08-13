package Shared;

import Server.GameSession.GameSessionBase;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {
    public void testConnection() throws RemoteException;

    public void initializeGame(GameSessionBase gameSession) throws RemoteException;

    public void updateGame(GameSessionBase gameSession) throws RemoteException;
}
