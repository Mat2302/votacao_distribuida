package servidor;

public class Candidate extends Serializable {
    private int id;
    private String name;

    Candidate(int id, String name){
        this.id = id;
        this.name = name;
    }

    public String getName(){ return this.name; }
    public int getId(){ return this.id; }
    @Override
    public String toString(){ return id + " - " + name; }
}
