package shared;

import java.io.Serializable;

public class Voter implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private String cpf;

    public Voter(String name, String cpf){
        this.cpf = cpf;
        this.name = name;
    }

    public String getName(){ return this.name; }
    public String getCPF(){ return this.cpf; }
}
