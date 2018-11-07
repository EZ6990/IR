package TextOperations.Parsers;

import TextOperations.Token;

import java.util.LinkedList;

public abstract class AbstractParser {

    private LinkedList<Token> txt;
    private int count;


    public AbstractParser() {
    }

    public LinkedList<Token> getTxt() {
        return txt;
    }

    public void setTxt(LinkedList<Token> txt) {
        this.txt = txt;
        count=0;
    }

    public abstract  void manipulate(Token token);

    public void operate() {
        Token toke = null;
        while (hasNext())
            toke=getNext();
        manipulate(toke);


    }

    protected boolean hasNext(){
        return count<txt.size();
    }


   protected Token getNext(){
       if(txt!=null) {
           return txt.get(count);
       }
       return null;

   }

}
