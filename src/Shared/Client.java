package Shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The `Client` interface defines the remote methods that a client application can invoke
 * on a remote object via RMI (Remote Method Invocation). It extends the `Remote` interface,
 * which is a marker interface for identifying remote objects.
 *
 * <p> This interface provides methods for testing the connection, initializing a game session,
 * updating a game session, and terminating a game session. These methods are designed to be
 * invoked remotely by a server, allowing for communication between the client and the server.
 *
 * <p> Any class that implements this interface must handle potential `RemoteException` instances
 * that can occur during remote method invocation.
 *
 * <p> This interface is designed to support distributed applications using RMI technology.
 *
 * <p> This interface is part of the shared package, intended for use by both the client and server components.
 *
 * @author
 *   - Aaron Barkan
 *   - Omer Bar
 * @version 1.0
 * @since August 2023
 */
public interface Client extends Remote {

    /**
     * Tests the connection between the client and the server.
     * This method is used to ensure that the remote communication is functional.
     *
     * @throws RemoteException If a remote communication error occurs.
     */
    void testConnection() throws RemoteException;

    /**
     * Initializes a game session on the client side.
     * This method is invoked by the server to provide the client with the initial game session state.
     *
     * @param gameSession The initial state of the game session.
     * @throws RemoteException If a remote communication error occurs.
     */
    void initializeGame(GameSession gameSession) throws RemoteException;

    /**
     * Updates the game session on the client side.
     * This method is invoked by the server to provide updates to the game session state.
     *
     * @param gameSession The updated state of the game session.
     * @throws RemoteException If a remote communication error occurs.
     */
    void updateGame(GameSession gameSession) throws RemoteException;

    /**
     * Terminates a game session on the client side with a specified message.
     * This method is invoked by the server to inform the client about the termination of a game session.
     *
     * @param message The message indicating the reason for game session termination.
     * @throws RemoteException If a remote communication error occurs.
     */
    void terminateGame(String message) throws RemoteException;
}
