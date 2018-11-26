package MapReduce;

public class DocumentTermInfo {

    private int rareCount;
    private String mostCommonName;
    private int mostCommonFreq;

    public DocumentTermInfo() {
        this.rareCount = 0;
        this.mostCommonName = "";
        mostCommonFreq=0;
    }

    public int getRareCount() {
        return rareCount;
    }

    public void addRareCount() {
        this.rareCount++;
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
}
