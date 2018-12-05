package MapReduce.Parse.Parsers;

import MapReduce.Parse.CountryInfo;
import IO.DataProvider;
import MapReduce.Parse.Term;
import MapReduce.Parse.AbstractTermDocumentInfo;
import MapReduce.Parse.CityTDI;
import TextOperations.Stemmer;
import TextOperations.Token;
import TextOperations.TokenizedDocument;
import java.util.HashMap;

public class CountryParser extends AbstractParser {

    private CountryInfo country = null;
    private int index;
    public CountryParser(HashMap<String, AbstractTermDocumentInfo> map, TokenizedDocument doc, Stemmer stemmer) {
        super(map, doc,stemmer);
        this.index = 0;
    }

    @Override
    public void manipulate() {
        int docSize = getTxtSize();
        while (this.index < docSize){
            country = null;
            Token token = get(this.index);
            if ((country = getCountryInfo((token.toString()))) != null)
                putInMap(token.toString());
            this.index++;
        }
    }

    @Override
    protected void putInMap(String s) {
        String capitalName = s.toUpperCase();
        CityTDI tmp = null;

        if (map.containsKey(capitalName)) {
            tmp = (CityTDI)map.get(capitalName);
            tmp.setFrequency(tmp.getFrequency() + 1);
            tmp.addLocation(this.index);
        } else {
            tmp = new CityTDI(new Term(capitalName,this.stemmer),this.document.getID(),this.country);
            tmp.addLocation(this.index);
            map.put(capitalName,tmp);
        }
    }

    private CountryInfo getCountryInfo(String CapitalName){
        return DataProvider.getCountryInfo(CapitalName);
    }
}
