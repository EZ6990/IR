package TextOperations;

import java.util.ArrayList;
import java.util.List;

public class DynamicFilter implements IFilter {


    public List<Token> tokenized_words;

    public DynamicFilter(List <Token> lst){
        this.tokenized_words = lst;
    }

    @Override
    public List<Token> filter(List<Token> lst) {
        List<Token> filtered_tokens =  new ArrayList<Token>(lst);
        filtered_tokens.removeAll(this.tokenized_words);
        return filtered_tokens;
    }

    @Override
    public boolean contains(Token token) {
        return this.tokenized_words.contains(token);
    }

    @Override
    public DynamicFilter substract(IFilter filter) {
        return new DynamicFilter(filter.filter(this.tokenized_words));
    }

    @Override
    public DynamicFilter intersection(IFilter filter) {
        DynamicFilter right = filter.substract(this);
        return this.substract(right);
    }
}
