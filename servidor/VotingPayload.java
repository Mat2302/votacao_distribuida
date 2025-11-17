package servidor;

import java.util.List;

public class VotingPayload {

    private String question;
    private List<Candidate> candidates;

    VotingPayload(String question, List<Candidate> candidates){
        this.question = question;
        this.candidates = candidates;
    }

    public String getQuestion(){ return this.question; }
    public List<Candidate> getCandidates(){ return this.candidates; };

}
