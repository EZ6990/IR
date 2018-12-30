package MapReduce.Parse;

import java.util.ArrayList;
import java.util.List;

public class DocumentTermInfo implements Info{

    private int numOfDifferentWords;
    private String mostCommonName;
    private int mostCommonFreq;
    private String documentName;
    private int numOfTerms;
    private List<String> entities;

    public DocumentTermInfo(String documentName) {
        this.numOfDifferentWords = 0;
        this.mostCommonName = "";
        this.mostCommonFreq = 0;
        this.documentName=documentName;
        this.numOfTerms=0;
        this.entities = new ArrayList<String>();
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

    public List<String> getEntities() {
        return entities;
    }

    public void addEntities(String entity) {
        this.entities.add(entity);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.documentName).append(";").append(this.mostCommonFreq).append("|").append(this.numOfDifferentWords).append("|").append(this.numOfTerms);
        if (this.entities.size() > 0) {
            stringBuilder.append("|");
            for (String entity : entities) {
                stringBuilder.append(entity).append("!");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DocumentTermInfo)
            return this.documentName.equals(((DocumentTermInfo)obj).getDocumentName());
        return false;
    }
}
