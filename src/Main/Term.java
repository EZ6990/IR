package Main;


import java.io.Serializable;

public class Term implements Serializable{

    public Term(String name){
        this.data = name;
    }
    private String data;

    public String getData() {
        return data;
    }
}
