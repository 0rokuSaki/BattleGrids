package Client;

import Shared.Client;
import Shared.Server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClientImpl implements Client, Runnable {

    private static final String HOST = "localhost";

    private static final int PORT = 54321;

    private static final int KEEPALIVE_SLEEP_TIME_MS = 1000;

    private static final ClientImpl instance = new ClientImpl();

    public static synchronized ClientImpl getInstance() {
        return instance;
    }

    private Lock lock;

    private String username;

    private Server serverStub;

    private boolean connectedToServer;

    private ClientImpl() {
        lock = new ReentrantLock();
        username = null;
        serverStub = null;
        connectedToServer = false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Server getServerStub() {
        return serverStub;
    }

    public void logOut() {
        lock.lock();
        try {
            serverStub.handleLogoutRequest(username);
        } catch (RemoteException ignored) {}
        username = null;
        serverStub = null;
        connectedToServer = false;
        lock.unlock();
    }

    @Override
    public void run() {
        while (true) {
            // Test connection to server; Attempt to reconnect if there is no connection
            if (connectedToServer) {
                connectedToServer = testConnectionToServer();
            } else {
                connectedToServer = connectToServer();
            }

            // Send keepalive to server
            if (connectedToServer && username != null) {
                sendKeepAliveToServer();
            }

            // Sleep some time
            try {
                Thread.sleep(KEEPALIVE_SLEEP_TIME_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean testConnectionToServer() {
        try {
            serverStub.probe();
            return true;
        } catch (RemoteException ignored) {}
        return false;
    }

    private boolean connectToServer() {
        try {
            // Locate server's registry
            Registry registry = LocateRegistry.getRegistry(HOST, PORT);

            // Obtain a reference to GameServer
            serverStub = (Server) registry.lookup("GameServer");

            // Update connection state
            return true;
        } catch (RemoteException | NotBoundException ignored) {}
        return false;
    }

    private boolean sendKeepAliveToServer() {
        try {
            return serverStub.handleKeepAlive(username);
        } catch (RemoteException remoteException) {
            remoteException.printStackTrace();
        }
        return false;
    }
}
