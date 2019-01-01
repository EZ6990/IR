package MapReduce.Parse.Parsers;

import IO.AbstractTokenizedDocument;
import MapReduce.Parse.AbstractTermDocumentInfo;
import TextOperations.*;

import java.util.HashMap;

public class WordParser extends AbstractParser {


    private IFilter ignore;

    public WordParser(HashMap<String, AbstractTermDocumentInfo> map, AbstractTokenizedDocument doc, Stemmer stemmer, IFilter ignore) {
        super(map, doc, stemmer);
        this.ignore = ignore;
    }

    @Override
    public void manipulate() {
        int i = 0, size = getTxtSize();
        String strToken;
        Token token;

//
        while (i < size) {
            token = get(i);
            strToken = token.toString();
            Double d;
            char c = strToken.charAt(0);


            if (this.ignore.contains(token)) {
                i++;
                continue;
            }

            Token newToken = new Token(strToken);
            if (strToken.length() == 0 || (strToken.length() == 1 && ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z')))
                    || (c == '$') || strToken.charAt(strToken.length() - 1) == '%' || isFraction(strToken)
                    || strToken.contains("-") || ((isNumber(newToken)) != null)) {
                i++;
                continue;


            } else if (strToken.length() > 1 && strToken.charAt(strToken.length() - 1) == 's' && strToken.charAt(strToken.length() - 2) == '\'') {
                if (strToken.length() > 2)
                    putInMap(strToken.substring(0, strToken.length() - 2));

            }
            else
                putInMap(strToken);
            i++;
        }


    }

    private Double isNumber(Token token) {
        if (token.isNumber())
            return new Double(Double.parseDouble(token.toString()));
        String s = token.toString().replace(",", "");
        try {
            return new Double(Double.parseDouble(s));

        } catch (Exception e) {
            return null;
        }
    }


    private void splitAndAdd(String[] split) {
        for (int i = 0; i < split.length; i++) {
            if (split[i].charAt(0) >= 'a' && split[i].charAt(0) <= 'z')
                putInMap(split[i]);
        }
    }

    @Override
    protected void putInMap(String s) {

        if (mapContains(s.toLowerCase()))
            super.putInMap(s.toLowerCase());

        else if (mapContains(s.toUpperCase()) && s.charAt(0) >= 'a') {
            super.putInMap(s);
            mapRemove(s.toUpperCase());
        } else if (s.charAt(0) <= 'Z')
            super.putInMap(s.toUpperCase());

        else super.putInMap(s);
    }

    private boolean isFraction(String string) {
        if (string.charAt(0) == '/' || string.charAt(string.length() - 1) == '/' || !string.contains("/"))
            return false;

        int count = 0;
        char c;
        for (int i = 0; i < string.length(); i++) {
            c = string.charAt(i);
            if ((c < '0' || c > '9') && c != '/')
                return false;
            else if (c == '/')
                count++;
        }

        return count == 1;


    }
}
