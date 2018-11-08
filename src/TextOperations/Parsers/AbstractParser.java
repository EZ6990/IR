package TextOperations.Parsers;

import TextOperations.Token;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractParser {

    private ConcurrentHashMap<String, Integer> map;
    private List<Token> txt;
    private int index;


    public AbstractParser() {
    }

    public AbstractParser(ConcurrentHashMap<String, Integer> map, List<Token> txt) {
        this.map = map;
        this.txt = txt;
    }

    public List<Token> getTxt() {
        return txt;
    }

    public void setTxt(List<Token> txt) {
        this.txt = txt;
        index = 0;
    }

    public abstract void manipulate();

    protected void putInMap(String s) {
        if (map.contains(s)) {
            Integer tmp = map.get(s);
          tmp =new Integer(tmp.intValue() + 1);
        }
        else {
            map.put(s,new Integer(1));
        }
    }




    protected boolean hasNext(){
        return index<txt.size();
    }



}
