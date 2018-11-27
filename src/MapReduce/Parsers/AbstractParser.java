package MapReduce.Parsers;

import Main.Term;
import MapReduce.TermDocumentInfo;
import TextOperations.Token;
import TextOperations.TokenizedDocument;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractParser {

    protected HashMap<String, TermDocumentInfo> map;
    protected TokenizedDocument document;
    private List<Token> txt;
    private int index;


    public AbstractParser(HashMap<String, TermDocumentInfo> map, TokenizedDocument doc) {
        this.map = map;
        this.document = doc;
        this.txt = doc.getTokenizedText();
    }


    protected Token get(int i) {
        return txt.get(i);
    }


    public void setTxt(List<Token> txt) {
        this.txt = txt;
        index = 0;
    }

    public abstract void manipulate();

    protected void putInMap(String s) {
        if (map.containsKey(s)) {
            TermDocumentInfo tmp = map.get(s);
            tmp.addToFrequency(1);
        } else {
            map.put(s, new TermDocumentInfo(new Term(s),this.document.getID()));
        }
    }


    protected int getTxtSize() {
        return txt.size();
    }

    protected boolean mapContains(String s) {
        return map.containsKey(
                s
        );
    }

    protected void mapRemove(String s) {
       map.get(s).addToFrequency(map.get(s.toUpperCase()).getFrequency());
       map.remove(s.toUpperCase());
    }

    protected boolean hasNext() {
        return index < txt.size();
    }


}
