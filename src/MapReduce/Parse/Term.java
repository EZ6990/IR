package MapReduce.Parse;


import TextOperations.Stemmer;

public class Term {

    private String data;
    private String stemmedData;
    private Stemmer stemmer;

    public Term(String name,Stemmer stemmer){
        this.data = name;
        this.stemmer = stemmer;
        this.stemmedData = null;
        if (this.stemmer != null);
            this.stemmedData = this.stemmer.stemString(this.data.trim());
    }

    public String getData() {
        return this.stemmedData == null ? this.data : stemmedData;
    }

    public void setData(String termData) {
        if (this.stemmedData == null)
            this.data  = termData;
        else
            this.stemmedData = termData;
    }
}
