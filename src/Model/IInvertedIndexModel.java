package Model;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public interface IInvertedIndexModel{

    public void StartInvertedIndex();
    public void ClearInvertedIndex();
    public void setCorpusLocation(String location);
    public void setStopWordsLocationLocation(String location);
    public void setPostLocation(String location);
    public void LoadDictionary();
    public List<String> getLanguage();
    public HashMap<String,String> getTermTF();
    public void setStemmer(boolean selected);
    public long getTimeToFinish();
    int getDocumentDictionaryLength();

    void SearchQueries(File f);
    void SearchQuery(String text);
}
