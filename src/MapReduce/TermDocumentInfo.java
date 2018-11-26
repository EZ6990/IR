package MapReduce;

import Main.Term;

public class TermDocumentInfo {

    private Term term;
    private int documentID;
    private int Frequency;

    public TermDocumentInfo(Term term,int documentID){
        this.term = term;
        this.documentID = documentID;
        this.Frequency = 1;
    }

    public int getFrequency() {
        return this.Frequency;
    }

    public Term getTerm() {
        return term;
    }

    public int getDocumentID() {
        return documentID;
    }

    public void setFrequency(int frequency) {
        this.Frequency = frequency;
    }
}
