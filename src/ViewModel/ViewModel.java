package ViewModel;

import Model.IInvertedIndexModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ViewModel extends Observable implements Observer {

    private final IInvertedIndexModel model;
    private SimpleStringProperty strCorpusLocation;
    private SimpleStringProperty strPostingLocation;
    private SimpleStringProperty strQuery;
    private SimpleBooleanProperty bStemming;
    private SimpleBooleanProperty bSemantic;
    private SimpleObjectProperty observableCountryItems;

    public ViewModel(IInvertedIndexModel model) {

        this.model = model;
        this.strCorpusLocation = new SimpleStringProperty("");
        this.strPostingLocation = new SimpleStringProperty("");
        this.strQuery = new SimpleStringProperty("");
        this.bStemming = new SimpleBooleanProperty(false);
        this.bSemantic = new SimpleBooleanProperty(false);
        ObservableList<String> tmp = FXCollections.observableArrayList();
        this.observableCountryItems = new SimpleObjectProperty(tmp);

    }

    public ObservableList<String> getObservableCountryItems() {
        return (ObservableList<String>)observableCountryItems.get();
    }

    public SimpleObjectProperty observableCountryItemsProperty() {
        return observableCountryItems;
    }
    public String getStrCorpusLocation() {
        return strCorpusLocation.get();
    }

    public SimpleStringProperty strCorpusLocationProperty() {
        return strCorpusLocation;
    }

    public String getStrPostingLocation() {
        return strPostingLocation.get();
    }

    public SimpleStringProperty strPostingLocationProperty() {
        return strPostingLocation;
    }

    public String getStrQuery() {
        return strQuery.get();
    }

    public SimpleStringProperty strQueryProperty() {
        return strQuery;
    }

    public boolean isbStemming() {
        return bStemming.get();
    }

    public SimpleBooleanProperty bStemmingProperty() {
        return bStemming;
    }

    public boolean isbSemantic() {
        return bSemantic.get();
    }

    public SimpleBooleanProperty bSemanticProperty() {
        return bSemantic;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == model){
            setChanged();
            notifyObservers(arg);
        }
    }

    public void startInvertedIndex(){
        this.model.setCorpusLocation(getStrCorpusLocation());
        this.model.setStopWordsLocationLocation(getStrCorpusLocation());
        this.model.setPostLocation(getStrPostingLocation());
        this.model.setStemmer(isbStemming());

        new Thread(() -> {this.model.StartInvertedIndex();}).start();
    }


    public void Clear(String location) {
        this.model.setPostLocation(location);
        this.model.ClearInvertedIndex();
    }

    public void LoadIndexers() {
        this.model.setPostLocation(getStrPostingLocation());
        this.model.setStemmer(isbStemming());
        this.model.LoadDictionary();
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
