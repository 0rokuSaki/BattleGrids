package Server;

import Shared.Server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class ServerImpl implements Server {

    @Override
    public ReturnCode handleLoginRequest(String username, String passwordHash) throws RemoteException {
        if (username.equals("0")) {
            return ReturnCode.NO_ERROR;
        }
        if (username.equals("1")) {
            return ReturnCode.INCORRECT_LOGIN_INFO;
        }
        return ReturnCode.GENERAL_ERROR;
    }

    @Override
    public ReturnCode handleRegistrationRequest(String username, String passwordHash) throws RemoteException {
        if (username.equals("0")) {
            return ReturnCode.NO_ERROR;
        }
        if (username.equals("1")) {
            return ReturnCode.USER_ALREADY_EXISTS;
        }
        return ReturnCode.GENERAL_ERROR;
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
