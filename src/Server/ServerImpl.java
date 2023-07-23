package Server;

import Shared.Server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class ServerImpl implements Server {

    private DBManagerDummy dbManager = new DBManagerDummy();

    @Override
    public String handleLoginRequest(String username, String passwordHash) throws RemoteException {
        String dbPasswordHash = dbManager.getPasswordHash(username);
        if (dbPasswordHash != null && dbPasswordHash.equals(passwordHash)) {
            return "";
        }
        return "Incorrect username and/or password";
    }

    @Override
    public String handleRegistrationRequest(String username, String passwordHash) throws RemoteException {
        if (dbManager.userExists(username)) {
            return "Username already exists";
        }
        if (username.equals("") || passwordHash.equals("")) {
            return "Invalid username and/or password";
        }
        dbManager.setUser(username, passwordHash);
        return "";
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
