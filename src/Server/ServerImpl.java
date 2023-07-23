package Server;

import Shared.Server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class ServerImpl implements Server {

    @Override
    public boolean handleLoginRequest(String username, String passwordHash) throws RemoteException {
        return false;
    }

    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        final int PORT = 54321;
        LocateRegistry.createRegistry(PORT);       // Create RMI Registry
        Server server = new ServerImpl();  // Instantiate GameServer object
        Server stub =
                (Server) UnicastRemoteObject.exportObject(server, 0);  // Export object
        LocateRegistry.getRegistry(PORT).bind("GameServer", stub);   // Bind stub
        System.out.println("Server is ready");
    }
}
