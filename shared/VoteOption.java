package shared;

import java.io.Serializable;

public class VoteOption implements Serializable {

    private static final long serialVersionUID = 1L;
    private String description;
    private int id;

    public VoteOption(int id, String description){
        this.id = id;
        this.description = description;
    }

    public String getDescription(){ return this.description; }
    public int getId(){ return this.id; }
}
