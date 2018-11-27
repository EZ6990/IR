package MapReduce;

import Main.Term;

public class TermDocumentInfo {

    private Term term;
    private String documentID;
    private int frequency;

    public TermDocumentInfo(Term term, String documentID) {
        this.term = term;
        this.documentID = documentID;
        this.frequency = 1;
    }

    public int getFrequency() {
        return this.frequency;
    }

    public Term getTerm() {
        return term;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void addToFrequency(int frequency) {
        this.frequency += frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
