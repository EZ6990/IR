package Main;


import TextOperations.Stemmer;

public class Term{

    private String data;
    private Stemmer stemmer;

    public Term(String name,Stemmer stemmer){
        this.data = name;
        this.stemmer = stemmer;
    }

    public String getData() {
        if (this.stemmer != null)
            return this.stemmer.stemString(this.data);
        return this.data.trim();
    }
}
