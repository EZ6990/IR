package MapReduce.Parse;

public class DocumentTermInfo implements Info{

    private int numOfDifferentWords;
    private String mostCommonName;
    private int mostCommonFreq;
    private String documentName;
    private int numOfTerms;
    private String entities;

    public DocumentTermInfo(String documentName) {
        this.numOfDifferentWords = 0;
        this.mostCommonName = "";
        this.mostCommonFreq = 0;
        this.documentName=documentName;
        this.numOfTerms=0;
        this.entities="";
    }
    public void addToNumOfTerms(int freq){numOfTerms+=freq;}

    public void setNumOfDifferentWords(int numOfDifferentWords) {
        this.numOfDifferentWords = numOfDifferentWords;
    }

    public String getMostCommonName() {
        return mostCommonName;
    }

    public void setMostCommonName(String mostCommonName) {
        this.mostCommonName = mostCommonName;
    }

    public int getMostCommonFreq() {
        return mostCommonFreq;
    }

    public void setMostCommonFreq(int mostCommonFreq) {
        this.mostCommonFreq = mostCommonFreq;
    }

    public String getDocumentName() {
        return documentName;
    }

    public String getEntities() {
        return entities;
    }

    public void setEntities(String entities) {
        this.entities = entities;
    }

    @Override
    public String toString() {
        return this.documentName + ";" + this.mostCommonFreq + "|" + this.numOfDifferentWords +"|" +this.numOfTerms;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DocumentTermInfo)
            return this.documentName.equals(((DocumentTermInfo)obj).getDocumentName());
        return false;
    }
}
