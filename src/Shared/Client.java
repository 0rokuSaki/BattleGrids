package Shared;

import Server.GameSession.GameSessionBase;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {
    void testConnection() throws RemoteException;

    void initializeGame(GameSessionBase gameSession) throws RemoteException;

    void updateGame(GameSessionBase gameSession) throws RemoteException;

    void terminateGame(String message) throws RemoteException;
}
