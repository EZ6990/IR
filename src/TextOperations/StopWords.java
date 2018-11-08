package TextOperations;

import java.util.ArrayList;
import java.util.List;

public class StopWords implements IFilter {

    private String [] stop_words;
    private List<Token> tokenized_stop_words;


    public StopWords(){
        this.stop_words = new String [] {"it","of"};
        this.tokenized_stop_words = new ArrayList<Token>();

        for (String stop_word : this.stop_words)
            this.tokenized_stop_words.add(new Token(stop_word));
    }


    @Override
    public List<Token> filter(List<Token> lst) {
        List<Token> filtered_tokens =  new ArrayList<Token>(lst);
        filtered_tokens.removeAll(this.tokenized_stop_words);
        return filtered_tokens;
    }
}
