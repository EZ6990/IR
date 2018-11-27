package MapReduce.Parsers;

import IO.HTTPWebRequest;
import Main.CountryInfo;
import Main.Term;
import MapReduce.CityTDI;
import MapReduce.TermDocumentInfo;
import TextOperations.TokenizedDocument;

import java.io.IOException;
import java.util.HashMap;

public class CountryParser extends AbstractParser {

    public CountryParser(HashMap<String, TermDocumentInfo> map, TokenizedDocument doc) {
        super(map, doc);
    }

    @Override
    public void manipulate() {
        int docSize = getTxtSize();
        int i = 0;
        while (i < docSize){
            CountryInfo country = null;
            if ((country = getCountryInfo((get(i).getWord()))) != null);
            new CityTDI(new Term(country.getCapitalName()))
            }
    }

    private CountryInfo getCountryInfo(String CapitalName){
        try {
            return new CountryInfo(CapitalName);
        } catch (IOException e) {
            return null;
        }
    }
}
