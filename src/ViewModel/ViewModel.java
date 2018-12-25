package ViewModel;

import MapReduce.Index.TermIndexer;
import Model.IInvertedIndexModel;
import View.TermIndexerData;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ViewModel extends Observable implements Observer {

    private final IInvertedIndexModel model;
    private SimpleStringProperty strCorpusLocation;
    private SimpleStringProperty strPostingLocation;
    private SimpleStringProperty strQuery;
    private SimpleBooleanProperty bStemming;
    private SimpleBooleanProperty bSemantic;
    private SimpleObjectProperty<ObservableList<String>> observableListViewItems;
    private ObservableList<String>observableLanguageItems;

    private SimpleObjectProperty observableTableVIew;


    public ViewModel(IInvertedIndexModel model) {

        this.model = model;
        this.strCorpusLocation = new SimpleStringProperty("");
        this.strPostingLocation = new SimpleStringProperty("");
        this.strQuery = new SimpleStringProperty("");
        this.bStemming = new SimpleBooleanProperty(false);
        this.bSemantic = new SimpleBooleanProperty(false);
        ObservableList<String> tmp = FXCollections.observableArrayList();
        this.observableListViewItems = new SimpleObjectProperty(new SortedList<String>(tmp,String.CASE_INSENSITIVE_ORDER));
        ObservableList<TermIndexer> tmp2 = FXCollections.observableArrayList();
        this.observableTableVIew = new SimpleObjectProperty(tmp2);
    }

    public Object getObservableTableVIew() {
        return observableTableVIew.get();
    }

    public SimpleObjectProperty observableTableVIewProperty() {
        return observableTableVIew;
    }

    public ObservableList<String> getObservableCountryItems() {
        return (ObservableList<String>)observableListViewItems.get();
    }

    public SimpleObjectProperty observableListViewItemsProperty() {
        return observableListViewItems;
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
    public void getTermTF(){
        for (Map.Entry<String,String> pair: this.model.getTermTF().entrySet())
            ((ObservableList<TermIndexerData>)this.observableTableVIew.get()).add(new TermIndexerData(pair.getKey(),pair.getValue()));
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

    public void Search() {
        String text = getStrQuery();
        File f = new File(text);
        if (f.exists())
            this.model.SearchQueries(f);
        else
            this.model.SearchQuery(text);
    }
}
