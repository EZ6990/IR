package TextOperations;

import java.util.ArrayList;
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

        List<Token> tmpList = new ArrayList<>();
        int size = lst.size();
        for (int i = 0; i < size; i++) {
            Matcher m = this.pattern.matcher(lst.get(i).getWord());
            if (m.find()) {
                if (m.group(1).length() > 0)
                    tmpList.add(new Token(m.group(1)));
            }

        }
        return tmpList;
    }

    @Override
    public boolean contains(Token token) {
        return token.getWord().matches(this.pattern.pattern());
    }

    @Override
    public DynamicFilter substract(IFilter filter) {
        return null;
    }

    @Override
    public DynamicFilter intersection(IFilter filter) {
        return null;
    }
}
