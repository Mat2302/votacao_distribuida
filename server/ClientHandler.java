package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

import client.CPFValidator;
import shared.NetCommand;
import shared.NetControl;
import shared.VoteOption;
import shared.VotePayload;
import shared.VotingInfoPayload;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final VotingInfoPayload votingInfoPayload;
    private final HashMap<String, Integer> votes;
    private final BarChartWindow chart;

    ClientHandler(Socket socket, VotingInfoPayload votingInfoPayload, HashMap<String, Integer> votes, BarChartWindow chart) {
        this.socket = socket;
        this.votingInfoPayload = votingInfoPayload;
        this.votes = votes;
        this.chart = chart;
    }

    @Override
    public void run() {
        try (
            Socket s = socket;
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream())
        ) {

            oos.writeObject(new NetControl(NetCommand.SendVotingInfo, votingInfoPayload));
            oos.flush();
            System.out.println("[+] Vote information sent.");

            Object obj = ois.readObject();

            if (!(obj instanceof NetControl)) {
                oos.writeObject(new NetControl(NetCommand.NotKnownProtocol));
                oos.flush();
                return;
            }

            NetControl nc = (NetControl) obj;
            System.out.println("[+] Received net control: " + nc.getNetCommand());

            if (nc.getNetCommand() == NetCommand.SendVote) {
                /*
                 * Recebe o payload do voto
                 * Verifica se é um cpf válido
                 * Verifica se é uma opção válida
                 * Registra o voto
                 * Encerra a conexão
                 */
                VotePayload payload = (VotePayload) nc.getPayload();

                if (!CPFValidator.validate(payload.getVoter().getCPF())) {
                    oos.writeObject(new NetControl(NetCommand.InvalidCPF));
                    oos.flush();
                    System.out.println("[+] Invalid CPF. Response sent.");
                    return;
                }

                boolean exists = false;
                for (VoteOption option : this.votingInfoPayload.getOptions()) {
                    if (option.getId() == payload.getVoteOptionID()) {
                        exists = true;
                    }
                }

                if (!exists) {
                    oos.writeObject(new NetControl(NetCommand.InvalidOption));
                    oos.flush();
                    System.out.println("[+] Invalid option received. Response sent.");
                    return;
                }

                votes.put(payload.getVoter().getCPF(), payload.getVoteOptionID());
                
                // Atualiza o gráfico a cada nova requisição
                String label = votingInfoPayload.getOptions()
                    .stream()
                    .filter(o -> o.getId() == payload.getVoteOptionID())
                    .findFirst()
                    .get()
                    .getDescription();
                
                long count = votes.values()
                      .stream()
                      .filter(v -> v == payload.getVoteOptionID())
                      .count();
                
                chart.updateValue(label, (int) count);
                
                System.out.println("Vote registered for " + payload.getVoter().getCPF() + " closing connection.");
                oos.writeObject(new NetControl(NetCommand.Shutdown));
                oos.flush();
                return;
            }

        } catch (IOException ioe) {
            System.err.println("I/O error with client " + socket.getRemoteSocketAddress() + ": " + ioe.getMessage());
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Class not found during deserialization for client " + socket.getRemoteSocketAddress() + ": " + cnfe.getMessage());
        } finally {
            System.out.println("ClientHandler finished for " + socket.getRemoteSocketAddress());
        }
    }
}
