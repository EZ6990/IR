package IO;

import IO.CountryInMemoryDB;
import MapReduce.Parse.CountryInfo;

import java.io.IOException;

public class DataProvider {


    private String ConfigFilePath;
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
//        if (country == null) {
//            try {
//                country = new CountryInfo(new HTTPWebRequest().post("https://restcountries.eu/rest/v2/Capital" + CapitalName + "?fields=name;capital;population;currencies"));
//            } catch (IOException e) {
//                country = null;
//            }
//        }
        return country;
    }

}
