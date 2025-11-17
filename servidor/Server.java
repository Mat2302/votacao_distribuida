package servidor;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Multi-threaded server: accepts multiple client connections and serves each connection
 * in its own thread (ClientHandler).
 */
public class Server
{
    public static void main(String[] args)
    {
        try
        {
            System.out.println("Server starting: " + NetProtocol.getLocalIpAddress() + " @ " + NetProtocol.port);

            ServerController controller = new ServerController();
            controller.start(); // will block until server stops (or run forever)
            System.out.println("Server is shutting down now.");
        }
        catch (Exception e)
        {
            System.err.println("Unexpected exception: " + e.getMessage());
        }
    }
}

/**
 * Controller that listens for incoming connections and dispatches each accepted socket
 * to a ClientHandler (thread) using an ExecutorService.
 */
class ServerController
{
    // Choose a thread pool strategy: cached pool adapts to the number of clients.
    // You can replace with a fixed thread pool if you want to limit concurrency.
    private final ExecutorService clientPool = Executors.newCachedThreadPool();

    // Use this flag to stop the server cleanly if you add shutdown logic later.
    private volatile boolean running = true;

    void start()
    {
        // Create ServerSocket and enter accept loop
        try (ServerSocket serverSocket = new ServerSocket(NetProtocol.port))
        {
            System.out.println("Server listening on port " + NetProtocol.port);

            // Accept loop: for each connection submit a handler to the pool
            while (running)
            {
                try
                {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Accepted connection from " + clientSocket.getRemoteSocketAddress());

                    // Submit a new handler for that client
                    clientPool.submit(new ClientHandler(clientSocket));
                }
                catch (IOException ioe)
                {
                    // If the server socket is closed while waiting, break the loop
                    if (running)
                        System.err.println("Error accepting client connection: " + ioe.getMessage());
                    else
                        System.out.println("Server stopped accepting connections.");
                }
            }
        }
        catch (IOException e)
        {
            System.err.println("Could not listen on port " + NetProtocol.port + ". " + e.getMessage());
        }
        finally
        {
            shutdownAndAwaitTermination();
        }
    }

    // Request orderly shutdown of the ExecutorService
    private void shutdownAndAwaitTermination()
    {
        clientPool.shutdown(); // stop accepting new tasks
        try
        {
            if (!clientPool.awaitTermination(10, TimeUnit.SECONDS))
            {
                clientPool.shutdownNow(); // cancel currently executing tasks
                if (!clientPool.awaitTermination(10, TimeUnit.SECONDS))
                {
                    System.err.println("Client pool did not terminate.");
                }
            }
        }
        catch (InterruptedException ie)
        {
            clientPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}