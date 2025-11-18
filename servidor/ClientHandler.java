package servidor;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

import shared.NetCommand;
import shared.NetControl;
import shared.VotingPayload;

/**
 * Handles interaction with a single connected client.
 * Each instance runs on the thread pool.
 */
public class ClientHandler implements Runnable
{
    private final Socket socket;
    private final VotingPayload votingPayload;
    private final Random randGen;

    ClientHandler(Socket socket, VotingPayload votingPayload)
    {
        this.socket = socket;
        this.votingPayload = votingPayload;
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

            System.out.println("sending voting info...");
            oos.writeObject(new NetControl(NetCommand.SendVotingInfo, votingPayload));
            oos.flush();

            // receber cpf
            // validar cpf

            // se validado e não existir voto:
            // enviar payload de votação
            // receber votação

            // VotingPayload votingInfo = new VotingPayload("qual candidato é o melhor?", candidates);


            System.out.println("Asking for graceful shutdown of the client " + s.getRemoteSocketAddress());
            oos.writeObject(new NetControl(NetCommand.Shutdown));
            oos.flush();
        }
        catch (IOException ioe)
        {
            System.err.println("I/O error with client " + socket.getRemoteSocketAddress() + ": " + ioe.getMessage());
        }
        // catch (ClassNotFoundException cnfe)
        // {
        //     System.err.println("Class not found during deserialization for client " + socket.getRemoteSocketAddress() + ": " + cnfe.getMessage());
        // }
        finally
        {
            System.out.println("ClientHandler finished for " + socket.getRemoteSocketAddress());
        }
    }
}
