package ViewModel;

import Model.IInvertedIndexModel;
import View.IView;
import javafx.scene.control.TextField;

import java.util.HashMap;
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


}
