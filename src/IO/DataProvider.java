package IO;

import IO.CountryInMemoryDB;
import MapReduce.Parse.CountryInfo;

import java.io.IOException;

public class DataProvider {


    private String ConfigFilePath;
    private static String stopWordsLocation;
    private static String corpusLocation;
    private static String postLocation;
    private static String prefixPost;
    private static CountryInMemoryDB CountryDB;

    public DataProvider(String ConfigFile){
        this.ConfigFilePath = ConfigFile;
        try {
            CountryDB = new CountryInMemoryDB("https://restcountries.eu/rest/v2/all?fields=name;capital;population;currencies");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static CountryInfo getCountryInfo(String CapitalName){
        CountryInfo country = null;
        if (CountryDB != null){
            country = CountryDB.getCountryByCapital(CapitalName);
        }
        return country;
    }

    public static void setStopWordsLocation(String stopWordsLocation) {
        DataProvider.stopWordsLocation = stopWordsLocation;
        DataProvider.stopWordsLocation += "\\" + "stop_words.txt";
    }

    public static void setCorpusLocation(String corpusLocation) {
        DataProvider.corpusLocation = corpusLocation;
    }

    public static void setPostLocation(String postLocation) {
        DataProvider.postLocation = postLocation;
    }
    public static String getStopWordsLocation() {
        return DataProvider.stopWordsLocation;
    }

    public static String getCorpusLocation() {
        return DataProvider.corpusLocation;
    }

    public static String getPostLocation() {
        return DataProvider.postLocation;
    }

    public static String getPrefixPost() {
        return DataProvider.prefixPost;
    }
    public static void setPrefixPost(String prefixPost) {
        DataProvider.prefixPost = prefixPost;
    }
}
