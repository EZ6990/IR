package Model;


import IO.DataProvider;
import TextOperations.Stemmer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

public class SimpleInvertedIndexModel extends Observable implements IInvertedIndexModel {


    private String strStopWordsLocation;
    private String strCorpusLocation;
    private String strPostLocation;
    private boolean bStemmer;
    private Master splinter;
    private long TimeToInvertIndex;

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
        this.TimeToInvertIndex = splinter.getTimeToInvertIndex();
        setChanged();
        notifyObservers("INVERTED_INDEX_DONE");
    }

    @Override
    public void ClearInvertedIndex() {
        File postDir = new File(this.strPostLocation);
        for (File f:postDir.listFiles()){
            f.delete();
        }
        this.splinter = null;
        setChanged();
        notifyObservers("CLEAR_DONE");
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
        setChanged();
        notifyObservers("LOAD_INVERTED_INDEX_DONE");
    }

    @Override
    public List<String> getLanguage() {
        List<String> lstLanguages = new ArrayList<>();
        for (String value : DataProvider.getInstance().getFP105().values()) {
            if (!lstLanguages.contains(value))
                lstLanguages.add(value);
        }
        return lstLanguages;
    }

    @Override
    public HashMap<String, String> getTermTF() {
        return this.splinter.getTermTF();
    }

    @Override
    public void setStemmer(boolean selected) {
        this.bStemmer = selected;
    }

    @Override
    public long getTimeToFinish() {
        return this.TimeToInvertIndex/1000;
    }

    @Override
    public int getDocumentDictionaryLength() {
        return this.splinter.getDocumentDictionarySize();
    }

    private void initializeMaster(){
        DataProvider provider = DataProvider.getInstance();
        provider.setStopWordsLocation(this.strCorpusLocation);
        provider.setCorpusLocation(this.strStopWordsLocation);
        provider.setPostLocation(this.strPostLocation);
        Stemmer stemmer = null;

        if (this.bStemmer) {
            stemmer = new Stemmer();
            provider.setPrefixPost("stem_");
        }
        else{
            stemmer = null;
            provider.setPrefixPost("no_stem_");
        }
        this.splinter = new Master(provider,stemmer);
    }
}
