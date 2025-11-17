package servidor;

public class Candidate {
    private int id;
    private String name;

    Candidate(int id, String name){
        this.id = id;
        this.name = name;
    }

    public String getName(){ return this.name; }
    public int getId(){ return this.id; }
}
