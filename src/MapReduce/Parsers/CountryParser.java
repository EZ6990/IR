package MapReduce.Parsers;

import IO.CountryInMemoryDB;
import IO.HTTPWebRequest;
import Main.CountryInfo;
import Main.DataProvider;
import Main.Term;
import MapReduce.AbstractTermDocumentInfo;
import MapReduce.CityTDI;
import MapReduce.TermDocumentInfo;
import TextOperations.Token;
import TextOperations.TokenizedDocument;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.HashMap;

public class CountryParser extends AbstractParser {

    private CountryInfo country = null;
    public CountryParser(HashMap<String, AbstractTermDocumentInfo> map, TokenizedDocument doc) {
        super(map, doc);
    }

    @Override
    public void manipulate() {
        int docSize = getTxtSize();
        int i = 0;
        while (i < docSize){
            country = null;
            Token token = get(i);
            if ((country = getCountryInfo((token.toString()))) != null)
                putInMap(token.toString());
            i++;
        }
    }

    @Override
    protected void putInMap(String s) {
        String capitalName = s.toUpperCase();
        if (map.containsKey(capitalName)) {
            AbstractTermDocumentInfo tmp = map.get(capitalName);
            tmp.setFrequency(tmp.getFrequency() + 1);
        } else {
            map.put(capitalName, new CityTDI(new Term((capitalName)),this.document.getID(),this.country));
        }
    }

    private CountryInfo getCountryInfo(String CapitalName){
        return DataProvider.getCountryInfo(CapitalName);
    }
}
