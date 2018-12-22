package ViewModel;

import Model.IInvertedIndexModel;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ViewModel extends Observable implements Observer {

    private final IInvertedIndexModel model;

    public ViewModel(IInvertedIndexModel model) {
        this.model = model;
    }
    @Override
    public void update(Observable o, Object arg) {
        if (o == model){
            setChanged();
            notifyObservers(arg);
        }
    }

    public void startInvertedIndex(String corpusLocation, String postLocation, boolean selected){
        this.model.setCorpusLocation(corpusLocation);
        this.model.setStopWordsLocationLocation(corpusLocation);
        this.model.setPostLocation(postLocation);
        this.model.setStemmer(selected);

        new Thread(() -> {this.model.StartInvertedIndex();}).start();
    }


    public void Clear(String location) {
        this.model.setPostLocation(location);
        this.model.ClearInvertedIndex();
    }

    public void LoadIndexers(String postLocation, boolean selected) {
        this.model.setPostLocation(postLocation);
        this.model.setStemmer(selected);
        this.model.LoadDictionary();
    }

    public HashMap<String,String> getDictionary() {
        return this.model.getTermTF();
    }

    public long updateTimeToFinish() {
        return this.model.getTimeToFinish();
    }

    public int getTermDictionaryLength(){
        return this.model.getTermTF().size();
    }

    public int getDocsDictionaryLength(){
        return this.model.getDocumentDictionaryLength();
    }

    public List<String> getLanguagesDictionary() {
        return this.model.getLanguage();
    }

    public void Search(String text) {
        File f = new File(text);
        if (f.exists())
            this.model.SearchQueries(f);
        else
            this.model.SearchQuery(text);
    }
}
