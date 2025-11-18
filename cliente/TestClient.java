package cliente;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import shared.NetCommand;
import shared.NetControl;

public class TestClient {

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 1234)) {

            System.out.println("Connected to server!");

            // Mesma regra: criar primeiro OOS, depois OIS
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream  ois = new ObjectInputStream(socket.getInputStream());

            // Aqui você poderia enviar o CPF, mas como seu servidor ainda não exige, não enviaremos nada.

            while (true) {
                Object obj = ois.readObject();

                if (obj instanceof NetControl nc) {
                    System.out.println("Received NetControl: " + nc.getNetCommand());

                    if (nc.getNetCommand() == NetCommand.SendVotingInfo) {
                        System.out.println("Voting payload recebido:");
                        System.out.println(nc.getPayload());
                    }

                    if (nc.getNetCommand() == NetCommand.Shutdown) {
                        System.out.println("Servidor pediu shutdown. Encerrando cliente.");
                        break;
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}
