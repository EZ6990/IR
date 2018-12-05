package MapReduce.Parse;

public class DocumentTermInfo implements Info{

    private int numOfDifferentWords;
    private String mostCommonName;
    private int mostCommonFreq;
    private String documentName;

    public DocumentTermInfo(String documentName) {
        this.numOfDifferentWords = 0;
        this.mostCommonName = "";
        this.mostCommonFreq = 0;
        this.documentName=documentName;
    }

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


    @Override
    public String toString() {
        return this.documentName + ";" + this.mostCommonFreq + "|" + this.numOfDifferentWords;
    }
}
