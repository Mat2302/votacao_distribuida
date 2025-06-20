package server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import shared.NetCommand;
import shared.NetControl;
import shared.VotingInfoPayload;

/**
 * Handles interaction with a single connected client.
 * Each instance runs on the thread pool.
 */
public class ClientHandler implements Runnable
{
    private final Socket socket;
    private final VotingInfoPayload VotingInfoPayload;

    ClientHandler(Socket socket, VotingInfoPayload VotingInfoPayload)
    {
        this.socket = socket;
        this.VotingInfoPayload = VotingInfoPayload;
    }

    @Override
    public void run()
    {
        // Important: create ObjectOutputStream BEFORE ObjectInputStream to avoid deadlock
        try (Socket s = socket;
             ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
             ObjectInputStream  ois = new ObjectInputStream(s.getInputStream()))
             {
                 
            System.out.println("sending voting info...");
            oos.writeObject(new NetControl(NetCommand.SendVotingInfo, VotingInfoPayload));
            oos.flush();
            
            while (true){
                Object obj = ois.readObject();

                if (obj instanceof NetControl nc) {
                    System.out.println("Received NetControl: " + nc.getNetCommand());

                    if (nc.getNetCommand() == NetCommand.SendVote) {
                        System.out.println("voto recebido");
                        System.out.println(nc.getPayload());
                    }

                    if (nc.getNetCommand() == NetCommand.Shutdown) {
                        System.out.println("Servidor pediu shutdown. Encerrando cliente.");
                        break;
                    }
                }
            }
            System.out.println("ClientHandler started for " + s.getRemoteSocketAddress());


            // receber voto
            // registrar voto, sobrescrever ou ignorar

            // desconectar
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
