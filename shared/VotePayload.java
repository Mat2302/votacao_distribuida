package shared;

public class VotePayload extends Payload {

    private Voter voter;
    private int optionId;

    public VotePayload(String message, int value, Voter voter, int optionId) {
        super(message, value);
        this.voter = voter;
        this.optionId = optionId;
    }
    
    public Voter getVoter(){ return this.voter; }
    public int getVoteOptionID(){ return this.optionId; }

    @Override
    public String toString(){
        return "Voter CPF: " + this.voter.getCPF() + "\nOptionID: " + this.optionId + "\n";
    }
}
