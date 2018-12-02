package TextOperations;

import java.util.ArrayList;
import java.util.List;

public class RulesWords implements IFilter {


    private String [] stop_words;
    private List<Token> tokenized_stop_stop_words;


    public RulesWords(){
        this.stop_words = new String [] {"Thousand","Million","Billion","Trillion","%","$","Percent","Percentage","Dollars","U.S.","January","February","March","April","May","June","July","August","September","October","November","December","Jan","Feb","Mar","Apr","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

        this.tokenized_stop_stop_words = new ArrayList<Token>();

        for (String stop_word : this.stop_words) {
            this.tokenized_stop_stop_words.add(new Token(stop_word));
            this.tokenized_stop_stop_words.add(new Token(stop_word.toUpperCase()));
            this.tokenized_stop_stop_words.add(new Token(stop_word.toLowerCase()));
        }
    }

    @Override
    public List<Token> filter(List<Token> lst) {
        List<Token> filtered_tokens =  new ArrayList<Token>(lst);
        filtered_tokens.removeAll(this.tokenized_stop_stop_words);
        return filtered_tokens;
    }

    @Override
    public boolean contains(Token token) {
        return this.tokenized_stop_stop_words.contains(token);
    }
}
