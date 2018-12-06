package ViewModel;

import Model.IInvertedIndexModel;
import View.IView;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class ViewModel extends Observable implements Observer {

    private final IInvertedIndexModel model;

    private HashMap<String,Integer> termTF;
    public ViewModel(IInvertedIndexModel model) {
        this.model = model;
    }
    @Override
    public void update(Observable o, Object arg) {
        if (o == model){
            if (((String)arg).equals("INVERTED INDEX DONE")){
                this.termTF = model.getTermTF();
            }
        }

    }

    public void startInvertedIndex(String corpusLocation, String postLocation){
        this.model.setCorpusLocation(corpusLocation);
        this.model.setStopWordsLocationLocation(corpusLocation);
        this.model.setPostLocation(postLocation);
        this.model.StartInvertedIndex();
    }


}
