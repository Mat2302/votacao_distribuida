package client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import shared.NetCommand;
import shared.NetControl;
import shared.VotePayload;
import shared.Voter;
import shared.VotingInfoPayload;

public class VoteClient {

    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private VotingInfoPayload votingInfo = null;

    public VotingInfoPayload getVotingInfo() {
        return this.votingInfo;
    }

    public VoteClient() throws Exception {
        try {
            this.socket = new Socket("localhost", 1234);
            System.out.println("Connected to server!");

            System.out.println(this.socket);
    
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());
    
            Object obj = ois.readObject();
            if (obj instanceof NetControl nc && nc.getNetCommand() == NetCommand.SendVotingInfo) {
                this.votingInfo = (VotingInfoPayload) nc.getPayload();
                System.out.println("Voting information received!");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void listenAsync() {
        Thread t = new Thread(() -> {
            try {
                while (true) {
                    Object obj = ois.readObject();

                    if (obj instanceof NetControl nc) {
                        System.out.println("Received: " + nc.getNetCommand());

                        if (nc.getNetCommand() == NetCommand.Shutdown) {
                            System.out.println("Servidor pediu shutdown. Encerrando cliente.");
                            break;
                        }

                        if (nc.getNetCommand() == NetCommand.SendVotingInfo) {
                            System.out.println("Recebi atualizações do payload");
                            this.votingInfo = (VotingInfoPayload) nc.getPayload();
                        }
                    }
                }

            } catch (Exception e) {
                System.err.println("Communication error: " + e.getMessage());
            }
        });

        t.start();
    }

    public void sendVote(Voter voter, int optionID) throws Exception {
        boolean exists = votingInfo.getOptions().stream().anyMatch(o -> o.getId() == optionID);

        if (!exists) throw new Exception("Option does not exist.");

        VotePayload payload = new VotePayload(null, 0, voter, optionID);

        oos.writeObject(new NetControl(NetCommand.SendVote, payload));
        oos.flush();
        System.out.println("Voto enviado!");
    }
}
