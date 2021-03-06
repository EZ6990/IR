package MapReduce.Parse.Parsers;

import IO.AbstractTokenizedDocument;
import MapReduce.Parse.Term;
import MapReduce.Parse.AbstractTermDocumentInfo;
import MapReduce.Parse.TermDocumentInfo;
import TextOperations.Stemmer;
import TextOperations.Token;
import TextOperations.TokenizedDocument;

import java.util.HashMap;
import java.util.List;

public abstract class AbstractParser {

    protected HashMap<String, AbstractTermDocumentInfo> map;
    protected AbstractTokenizedDocument document;
    private List<Token> txt;
    private int index;
    protected Stemmer stemmer;


    public AbstractParser(HashMap<String, AbstractTermDocumentInfo> map, AbstractTokenizedDocument doc, Stemmer stemmer) {
        this.map = map;
        this.document = doc;
        this.txt = doc.getTokenizedText();
        this.stemmer = stemmer;
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
            AbstractTermDocumentInfo tmp = map.get(s);
            tmp.addToFrequency(1);
        } else {
            map.put(s, new TermDocumentInfo(new Term(s,this.stemmer),this.document.getID()));
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
