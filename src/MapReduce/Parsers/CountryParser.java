package MapReduce.Parsers;

import IO.CountryInMemoryDB;
import IO.HTTPWebRequest;
import Main.CountryInfo;
import Main.DataProvider;
import Main.Term;
import MapReduce.CityTDI;
import MapReduce.TermDocumentInfo;
import TextOperations.Token;
import TextOperations.TokenizedDocument;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.HashMap;

public class CountryParser extends AbstractParser {

    private CountryInfo country = null;
    public CountryParser(HashMap<String, TermDocumentInfo> map, TokenizedDocument doc) {
        super(map, doc);
    }

    @Override
    public void manipulate() {
        int docSize = getTxtSize();
        int i = 0;
        while (i < docSize){
            country = null;
            Token token = get(i);
            if ((country = getCountryInfo((token.toString()))) != null);
                putInMap(token.toString());
            }
    }

    @Override
    protected void putInMap(String s) {
        if (map.containsKey(s)) {
            TermDocumentInfo tmp = map.get(s);
            tmp.setFrequency(tmp.getFrequency() + 1);
        } else {
            map.put(s.toUpperCase(), new CityTDI(new Term((this.country.getCapitalName().toUpperCase())),this.document.getID(),this.country));
        }
    }

    private CountryInfo getCountryInfo(String CapitalName){
        return DataProvider.getCountryInfo(CapitalName);
    }
}
