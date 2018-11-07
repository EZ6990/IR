package TextOperations.Parsers;

import TextOperations.Token;

import java.util.LinkedList;

public abstract class AbstractParser {

    private LinkedList<Token> txt;
    private int index;


    public AbstractParser() {
    }

    public LinkedList<Token> getTxt() {
        return txt;
    }

    public void setTxt(LinkedList<Token> txt) {
        this.txt = txt;
        index=0;
    }

    public abstract  void manipulate(Token token);

    public void operate() {
        Token toke = null;
        while (hasNext()) {
            toke = txt.get(index);
            ++index;
            manipulate(toke);
        }

    }

    protected boolean hasNext(){
        return index<txt.size();
    }



}
