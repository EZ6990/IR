package TextOperations;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemoveFromEndnStart implements IFilter {



    private Pattern pattern;

    public RemoveFromEndnStart(){
        this.pattern = Pattern.compile("^[\\.,]*(.*?)[\\.,]*$");
    }

    @Override
    public List<Token> filter(List<Token> lst) {

        int size = lst.size();
        for (int i = 0; i < size; i++) {
            Matcher m = this.pattern.matcher(lst.get(i).getWord());
            if (m.find()) {
                lst.remove(i);
                lst.add(i,new Token(m.group(1)));
            }
        }
        return lst;
    }

    @Override
    public boolean contains(Token token) {
        return token.getWord().matches(this.pattern.pattern());
    }
}
