package Model;


import IO.DataProvider;
import TextOperations.Stemmer;
import javafx.beans.InvalidationListener;

import javax.xml.crypto.Data;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

public class SimpleInvertedIndexModel extends Observable implements IInvertedIndexModel {


    private String strStopWordsLocation;
    private String strCorpusLocation;
    private String strPostLocation;
    private boolean bStemmer;
    private Master splinter;

    public SimpleInvertedIndexModel(){
        this.splinter = null;
    }

    @Override
    public void StartInvertedIndex() {

        initializeMaster();
        
        try {
            this.splinter.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void ClearInvertedIndex() {
        File postDir = new File(this.strPostLocation);
        for (File f:postDir.listFiles()){
            f.delete();
        }
        this.splinter = null;
    }

    @Override
    public void setCorpusLocation(String location) {
        this.strCorpusLocation = location;
    }

    @Override
    public void setStopWordsLocationLocation(String location) {
        this.strStopWordsLocation = location;
    }

    @Override
    public void setPostLocation(String location) {
        this.strPostLocation = location;
    }

    @Override
    public void LoadDictionary() {
        initializeMaster();
        this.splinter.LoadTermIndex();
        this.splinter.LoadCityIndexer();
        this.splinter.LoadDocumentIndexer();
    }

    @Override
    public List<String> getLanguage() {
        return null;
    }

    @Override
    public HashMap<String, Integer> getTermTF() {
        return null;
    }

    @Override
    public void setStemmer(boolean selected) {
        this.bStemmer = selected;
    }

    private void initializeMaster(){
        DataProvider provider = new DataProvider("");
        DataProvider.setStopWordsLocation(this.strCorpusLocation);
        DataProvider.setCorpusLocation(this.strStopWordsLocation);
        DataProvider.setPostLocation(this.strPostLocation);
        Stemmer stemmer = null;

        if (this.bStemmer)
            stemmer = new Stemmer();

        this.splinter = new Master(provider,stemmer);
    }
}
