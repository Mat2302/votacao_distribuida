package servidor;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * Handles interaction with a single connected client.
 * Each instance runs on the thread pool.
 */
public class ClientHandler implements Runnable
{
    private final Socket socket;
    private final Random randGen;

    ClientHandler(Socket socket)
    {
        this.socket = socket;
        // Use a separate Random per handler to reduce contention
        this.randGen = new Random(System.nanoTime() ^ socket.hashCode());
    }

    @Override
    public void run()
    {
        // Important: create ObjectOutputStream BEFORE ObjectInputStream to avoid deadlock
        try (Socket s = socket;
             ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
             ObjectInputStream  ois = new ObjectInputStream(s.getInputStream()))
        {
            System.out.println("ClientHandler started for " + s.getRemoteSocketAddress());

            // receber cpf
            // validar cpf

            // se validado e não existir voto:
            // enviar payload de votação
            // receber votação

            System.out.println("Asking for graceful shutdown of the client " + s.getRemoteSocketAddress());
            oos.writeObject(new NetControl(NetCommand.Shutdown));
            oos.flush();
        }
        catch (IOException ioe)
        {
            System.err.println("I/O error with client " + socket.getRemoteSocketAddress() + ": " + ioe.getMessage());
        }
        catch (ClassNotFoundException cnfe)
        {
            System.err.println("Class not found during deserialization for client " + socket.getRemoteSocketAddress() + ": " + cnfe.getMessage());
        }
        finally
        {
            System.out.println("ClientHandler finished for " + socket.getRemoteSocketAddress());
        }
    }
}
