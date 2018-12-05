package TextOperations;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemoveFromEndnStart implements IFilter {


    private char [] remove;

    public RemoveFromEndnStart(char [] remove){
        this.remove = remove;
    }

    @Override
    public List<Token> filter(List<Token> lst) {

        List<Token> tmpList = new ArrayList<>();
        for (Token token : lst) {
            char [] chars = token.getWord().toCharArray();
            int posStart = 0;
            int posEnd = chars.length - 1;
            boolean bFound = true;
            while (posStart < chars.length && bFound) {
                bFound = false;
                for (char cut : this.remove) {
                    if (chars[posStart] == cut) {
                        posStart++;
                        bFound = true;
                    }
                }
            }
            bFound = true;
            while (posEnd > posStart && bFound) {
                bFound = false;
                for (char cut : this.remove) {
                    if (chars[posEnd] == cut) {
                        posEnd--;
                        bFound = true;
                    }
                }
            }
            if (posStart < posEnd + 1)
                tmpList.add(new Token(new String(chars, posStart, posEnd + 1 - posStart)));
        }
        return tmpList;
    }

    @Override
    public boolean contains(Token token) {
        return false;
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
