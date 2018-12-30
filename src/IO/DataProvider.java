package IO;

import MapReduce.Index.CityIndexer;
import MapReduce.Index.DocumentIndexer;
import MapReduce.Index.TermIndexer;
import MapReduce.Parse.CountryInfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DataProvider {

    private static DataProvider Instance;

    private String ConfigFilePath;
    private String stopWordsLocation;
    private String corpusLocation;
    private String postLocation;
    private String queriesLocation;
    private String prefixPost;
    private HashMap<String,List<String>> queriesResult;
    private CountryInMemoryDB CountryDB;
    private ConcurrentHashMap<String,String> FP104;
    private ConcurrentHashMap<String,String> FP105;

    private TermIndexer termIndexer;
    private DocumentIndexer documentIndexer;
    private CityIndexer cityIndexer;


    public ConcurrentHashMap<String, String> getFP104() {
        return FP104;
    }

    public ConcurrentHashMap<String, String> getFP105() {
        return FP105;
    }

    private DataProvider(){
        this.ConfigFilePath = "";
        try {
            this.CountryDB = new CountryInMemoryDB("https://restcountries.eu/rest/v2/all?fields=name;capital;population;currencies");
            this.stopWordsLocation = null;
            this.corpusLocation = null;
            this.postLocation = null;
            this.prefixPost = null;
            this.queriesResult = new HashMap<String,List<String>>();
            this.FP104 = new ConcurrentHashMap<>();
            this.FP105 = new ConcurrentHashMap<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public CountryInfo getCountryInfo(String CapitalName){
        CountryInfo country = null;
        if (this.CountryDB != null){
            country = this.CountryDB.getCountryByCapital(CapitalName);
        }
        return country;
    }

    public void setStopWordsLocation(String stopWordsLocation) {
        this.stopWordsLocation = stopWordsLocation;
        this.stopWordsLocation += "\\" + "stop_words.txt";
    }

    public void setCorpusLocation(String corpusLocation) {
        this.corpusLocation = corpusLocation;
    }

    public void setPostLocation(String postLocation) {
        this.postLocation = postLocation;
    }

    public String getQueriesLocation() {
        return this.queriesLocation;
    }

    public void setQueriesLocation(String location) {
        this.queriesLocation = location;
    }

    public String getStopWordsLocation() {
        return this.stopWordsLocation;
    }

    public String getCorpusLocation() {
        return this.corpusLocation;
    }

    public String getPostLocation() {
        return this.postLocation;
    }

    public String getPrefixPost() {
        return this.prefixPost;
    }

    public void setPrefixPost(String prefixPost) {
        this.prefixPost = prefixPost;
    }

    public TermIndexer getTermIndexer() {
        return termIndexer;
    }

    public DocumentIndexer getDocumentIndexer() {
        return documentIndexer;
    }

    public CityIndexer getCityIndexer() {
        return cityIndexer;
    }

    public void LoadTermIndex() {
        if (this.termIndexer == null)
            this.termIndexer = new TermIndexer(DataProvider.getInstance().getPostLocation() + "\\" + DataProvider.getInstance().getPrefixPost() + "termIndexer.index");

        this.termIndexer.read();

        System.out.println("Done Load Term Indexer");
    }

    public void LoadCityIndexer() {
        if (this.cityIndexer == null)
            this.cityIndexer = new CityIndexer(DataProvider.getInstance().getPostLocation() + "\\" + DataProvider.getInstance().getPrefixPost() + "cityIndexer.index");

        this.cityIndexer.read();

        System.out.println("Done Load City Indexer");
    }

    public void LoadDocumentIndexer() {
        if (this.documentIndexer == null)
            this.documentIndexer = new DocumentIndexer(DataProvider.getInstance().getPostLocation() +"\\" + DataProvider.getInstance().getPrefixPost() + "documentIndexer.index");

        this.documentIndexer.read();

        System.out.println("Done Load Document Indexer");
    }

    public void setTermIndexer(TermIndexer termIndexer) {
        this.termIndexer = termIndexer;
    }

    public void setDocumentIndexer(DocumentIndexer documentIndexer) {
        this.documentIndexer = documentIndexer;
    }

    public void setCityIndexer(CityIndexer cityIndexer) {
        this.cityIndexer = cityIndexer;
    }

    public static DataProvider getInstance() {

        if (Instance == null) {
            Instance = new DataProvider();
        }
        return Instance;
    }

    public void addRankedDocumentsForQuery(String query, List<String> docs) {
        this.queriesResult.put(query,docs);
    }

    public void clearQueriesResult() {
        this.queriesResult.clear();
    }

    public HashMap<String, List<String>> getQueriesResult() {
        return this.queriesResult;
    }
}
