package IO;

import IO.CountryInMemoryDB;
import MapReduce.Parse.CountryInfo;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class DataProvider {


    private static DataProvider Instance;

    private String ConfigFilePath;
    private String stopWordsLocation;
    private String corpusLocation;
    private String postLocation;
    private String prefixPost;
    private CountryInMemoryDB CountryDB;
    private ConcurrentHashMap<String,String> FP104;
    private ConcurrentHashMap<String,String> FP105;


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

    public static DataProvider getInstance() {

        if (Instance == null) {
            Instance = new DataProvider();
        }
        return Instance;
    }
}
