package client;

import java.io.ObjectOutputStream;
import java.util.List;

import shared.NetCommand;
import shared.NetControl;
import shared.VoteOption;
import shared.VotePayload;
import shared.VotingInfoPayload;
import shared.Voter;

public class VoteService {
    public static void sendVote(VotingInfoPayload votingInfo, Voter voter, int optionID, ObjectOutputStream oos) throws Exception {
        List<VoteOption> options = votingInfo.getOptions();
        
        boolean exists = false;
        for (VoteOption option: options){
            if (option.getId() == optionID){ exists = true; }
        }

        if (!exists) { throw new Exception("The option does not exists."); }

        VotePayload payload = new VotePayload(null, 0, voter, optionID);

        oos.writeObject(new NetControl(NetCommand.SendVote, payload));
        oos.flush();
        System.out.println("Voto enviado para o servidor!");
    }
}
