package shared;

import java.util.List;

public class VotingInfoPayload extends Payload {

    private static final long serialVersionUID = 1L;
    private String question;
    private String title;
    private String description;
    private List<VoteOption> options;

    public VotingInfoPayload(String message, int value, String question, List<VoteOption> options, String title, String description){
        super(message, value);
        this.question = question;
        this.options = options;
        this.title = title;
        this.description = description;
    }

    public String getQuestion(){ return this.question; }
    public String getTitle(){ return this.title; }
    public String getDescription(){ return this.description; }
    public List<VoteOption> getOptions(){ return this.options; };

    @Override
    public String toString(){
        String content = "Title: " + this.title + "\nDescription: " + this.description + "\nQuestion: " + this.question + "\nOptions:\n";
        for (VoteOption opt : this.options){
            content += "  -" + opt.getId() + " " + opt.getDescription() + "\n";
        }
        return content;
    }

}
