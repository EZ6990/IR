package MapReduce.Parsers;

import MapReduce.AbstractTermDocumentInfo;
import TextOperations.Token;
import TextOperations.TokenizedDocument;
import java.util.HashMap;

public class WordParser extends AbstractParser {


    public WordParser(HashMap<String, AbstractTermDocumentInfo> map, TokenizedDocument doc) {
        super(map, doc);

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
//            Double d;
//            char c = strToken.charAt(0);
//
//            while (strToken.charAt(strToken.length() - 1) == '.' || strToken.charAt(strToken.length() - 1) == ',')
//                strToken = strToken.substring(0, strToken.length() - 1);
//            Token newToken = new Token(strToken);
//            if (strToken.length() == 0 || (strToken.length() == 1 && ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z')))
//                    || (c == '$') || strToken.charAt(strToken.length() - 1) == '%' || isFraction(strToken)
//                    || strToken.contains("-") || ((isNumber(newToken)) != null)) {
//                i++;
//                continue;
//            }
//            else putInMap(strToken);

                putInMap(strToken);
            i++;
        }


    }

    private Double isNumber(Token token) {
        if (token.isNumber())
            return new Double(Double.parseDouble(token.toString()));
        String s = token.toString().replace(",", "");
        while (s.length() > 0 && s.charAt(s.length() - 1) == '.')
            s = s.substring(0, s.length() - 1);
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
