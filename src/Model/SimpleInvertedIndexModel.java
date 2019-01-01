package Model;


import IO.DataProvider;
import MapReduce.Parse.DocumentTermInfo;
import MapReduce.Parse.TermDocumentInfo;
import TextOperations.Stemmer;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SimpleInvertedIndexModel extends Observable implements IInvertedIndexModel {


    private String strStopWordsLocation;
    private String strCorpusLocation;
    private String strPostLocation;
    private String strQueriesLocation;

    private boolean bStemmer;
    private boolean bSemantic;
    private Master splinter;


    private IRMaster irsplinter;

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
        DataProvider provider = DataProvider.getInstance();
        provider.setStopWordsLocation(this.strCorpusLocation);
        provider.setCorpusLocation(this.strStopWordsLocation);
        provider.setPostLocation(this.strPostLocation);

        if (this.bStemmer) {
            provider.setPrefixPost("stem_");
        }
        else{
            provider.setPrefixPost("no_stem_");
        }

        DataProvider.getInstance().LoadTermIndex();
        DataProvider.getInstance().LoadCityIndexer();
        DataProvider.getInstance().LoadDocumentIndexer();
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
        return DataProvider.getInstance().getTermIndexer().getTermNumberOfOccurrenceMap();
    }

    @Override
    public void setStemmer(boolean selected) {
        this.bStemmer = selected;
    }

    @Override
    public void setSemantic(boolean selected) {
        this.bSemantic = selected;
    }

    @Override
    public long getTimeToFinish() {
        return this.TimeToInvertIndex/1000;
    }

    @Override
    public int getDocumentDictionaryLength() {
        return this.splinter.getDocumentDictionarySize();
    }

    @Override
    public void SearchQueries(File f,List<String> Cities) {
        this.strQueriesLocation = f.getAbsolutePath();
        DataProvider.getInstance().setQueriesLocation(this.strQueriesLocation);
        initializeIRMaster();
        try {
            this.irsplinter.start(null,Cities);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveQueryResults(String path) {
        File file=new File(path);
        try {
            this.irsplinter.printQueriesToFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void SearchQuery(String text,List<String> Cities) {
        DataProvider.getInstance().setQueriesLocation(null);
        initializeIRMaster();
        try {
            this.irsplinter.start(text,Cities);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getCountries() {
        List<String> lstCountries = new ArrayList<>();
        if (DataProvider.getInstance().getFP104() != null && DataProvider.getInstance().getFP104().size() > 0) {
            for (String value : DataProvider.getInstance().getFP104().values())
                if (!lstCountries.contains(value))
                    lstCountries.add(value);
        }
        else {
            Iterator it = DataProvider.getInstance().getCityIndexer().iterator();
            while(it.hasNext()){
                Map.Entry pair = (Map.Entry)it.next();
                lstCountries.add(pair.getKey().toString());
               // it.remove();
            }
        }

        return lstCountries;
    }

    @Override
    public void clearQueriesResult() {
        DataProvider.getInstance().clearQueriesResult();
    }

    public Set<String> getQueriesResult() {
     //   return DataProvider.getInstance().getQueriesResult().keySet();
        List<QueryResult> qs=this.irsplinter.getQueriesSearched();
        Set<String> set=new LinkedHashSet<>();
        for(QueryResult qr:qs)
            set.add(qr.getQueryId());
        return set;
    }

    @Override
    public List<String> getQueriesResultById(String id) {
        List<QueryResult> qs=this.irsplinter.getQueriesSearched();
        List<String>ans=new ArrayList<>();
        for (QueryResult qr :
                qs) {
            if (qr.getQueryId().equals(id)){
            List<String> docs=qr.getDocs();
                ans=docs.subList(0,docs.size() >= 50 ? 50 : docs.size());

            }
        }
        return ans;

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
        this.splinter = new Master(stemmer);
    }

    private void initializeIRMaster(){
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
        this.irsplinter = new IRMaster(stemmer,this.bSemantic);
    }

    @Override
    public List<String> getDocumentEntitiesByDocumentID(String id) {
        DocumentTermInfo info = this.irsplinter.getDocumentInfoByDocumentID(id);
        if (info != null)
            return info.getEntities();
        return null;
    }

    @Override
    public int getTermDocumentFrequencyByID(String termId,String docId) {
        TermDocumentInfo info = this.irsplinter.getTermInfoByTermID(termId,docId);
        if (info != null){
            return info.getFrequency();
        }
        return 0;
    }
}
