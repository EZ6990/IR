package MapReduce.Parsers;

import IO.HTTPWebRequest;
import MapReduce.TermDocumentInfo;
import TextOperations.TokenizedDocument;

import java.util.HashMap;

public class CountryParser extends AbstractParser {

    public CountryParser(HashMap<String, TermDocumentInfo> map, TokenizedDocument doc) {
        super(map, doc);
    }

    @Override
    public void manipulate() {

    }
}
