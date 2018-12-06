package Model;

import javafx.beans.Observable;

import java.util.HashMap;
import java.util.List;
import java.util.Observer;

public interface IInvertedIndexModel{

    public void StartInvertedIndex();
    public void ClearInvertedIndex();
    public void setCorpusLocation(String location);
    public void setStopWordsLocationLocation(String location);
    public void setPostLocation(String location);
    public void LoadDictionary();
    public List<String> getLanguage();
    public HashMap<String,Integer> getTermTF();






}
