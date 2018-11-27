package MapReduce.Parsers;

import IO.HTTPWebRequest;
import Main.CountryInfo;
import Main.Term;
import MapReduce.CityTDI;
import MapReduce.TermDocumentInfo;
import TextOperations.Token;
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
            Token token = get(i);
            if ((country = getCountryInfo((token.toString()))) != null);
                putInMap(token.toString());


            }
    }

    @Override
    protected void putInMap(String s) {
        HashMap <
        if (map.containsKey(s)) {
            TermDocumentInfo tmp = map.get(s);
            tmp.setFrequency(tmp.getFrequency() + 1);
        } else {
            map.put(s, new TermDocumentInfo(new Term(s),this.document.getID()));
        }
        new CityTDI(new Term(country.getCapitalName()), this.document.getID(),country);
    }

    private CountryInfo getCountryInfo(String CapitalName){
        try {
            return new CountryInfo(CapitalName);
        } catch (IOException e) {
            return null;
        }
    }
}
