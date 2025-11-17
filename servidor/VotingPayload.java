package servidor;

public class VotingPayload {

    private String question;
    private Candidate[] candidates;

    VotingPayload(String question, Candidate[] candidates){
        this.question = question;
        this.candidates = candidates;
    }

    public String getQuestion(){ return this.question; }
    public Candidate[] getCandidates(){ return this.candidates; };

}
