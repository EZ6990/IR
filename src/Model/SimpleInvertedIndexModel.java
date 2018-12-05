package Model;


import javafx.beans.InvalidationListener;

import java.util.HashMap;
import java.util.List;
import java.util.Observable;

public class SimpleInvertedIndexModel extends Observable implements IInvertedIndexModel {


    private String strStopWordsLocation;
    private String strCorpusLocation;

    @Override
    public void StartInvertedIndex() {

    }

    @Override
    public void ClearInvertedIndex() {

    }

    @Override
    public void setCorpusLocation(String location) {

    }

    @Override
    public void setStopWordsLocationLocation(String location) {

    }

    @Override
    public void setDictionaryLocation(String location) {

    }

    @Override
    public void LoadDictionary() {

    }

    @Override
    public List<String> getLanguage() {
        return null;
    }

    @Override
    public HashMap<String, Integer> getDictionary() {
        return null;
    }
}
