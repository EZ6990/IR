package TextOperations;

import java.util.ArrayList;
import java.util.List;

public class Tokenize {

    private String [] seperators;


    public Tokenize(){
        this.seperators = new String [] {" ","\t","\n","\r","\\{","\\}","\\(","\\)","\\[","\\]","\\!","\\?","\\\"","\\|",";"};
    }

    public List<Token> Tokenize(String text){
        ArrayList<Token> tokenized_text = new ArrayList<Token>();
        String [] splited_text = text.split(String.join("|",this.seperators));

        for (String word : splited_text) {
            if(word.length() > 0)
                tokenized_text.add(new Token(word));
        }

        return tokenized_text;
    }
}
