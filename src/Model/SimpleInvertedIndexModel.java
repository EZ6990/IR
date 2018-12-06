package Model;


import IO.DataProvider;
import javafx.beans.InvalidationListener;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

public class SimpleInvertedIndexModel extends Observable implements IInvertedIndexModel {


    private String strStopWordsLocation;
    private String strCorpusLocation;
    private String strPostLocation;

    @Override
    public void StartInvertedIndex() {
        DataProvider provider = new DataProvider("");
        DataProvider.setStopWordsLocation(this.strCorpusLocation);
        DataProvider.setCorpusLocation(this.strStopWordsLocation);
        DataProvider.setPostLocation(this.strPostLocation);

        Master master = new Master(provider,null);
        try {
            master.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void ClearInvertedIndex() {
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

    }

    @Override
    public List<String> getLanguage() {
        return null;
    }

    @Override
    public HashMap<String, Integer> getTermTF() {
        return null;
    }
}
