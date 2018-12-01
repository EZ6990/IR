package MapReduce.Parsers;

import MapReduce.TermDocumentInfo;
import TextOperations.Token;
import TextOperations.TokenizedDocument;

import java.util.HashMap;

public class WordParser extends AbstractParser {


    public WordParser(HashMap<String, TermDocumentInfo> map, TokenizedDocument doc) {
        super(map, doc);

    }

    @Override
    public void manipulate() {
        int i = 0, size = getTxtSize(), suffix, prefix;
        String strToken;
        Token token;

        while (i < size ) {
            token = get(i);
            strToken = token.toString();
            suffix = strToken.length() - 1;
            prefix = 0;
            boolean bSuff = true;
            boolean bPreff = true;
            if (!token.isNumber() && !isFraction(strToken)) {
                while (prefix < suffix) {


                    char cSuff = strToken.charAt(suffix);
                    char cPreff = strToken.charAt(prefix);

                    if (!(cSuff <= 'z' && cSuff >= 'a') && !(cSuff <= 'Z' && cSuff >= 'A')) {
                        --suffix;
                        bSuff = true;
                    } else
                        bSuff = false;

                    if (!(cPreff <= 'z' && cPreff >= 'a') && !(cPreff <= 'Z' && cPreff >= 'A')) {
                        ++prefix;
                        bPreff = true;
                    } else
                        bPreff = false;
                }

                strToken = strToken.substring(prefix, suffix + 1);


                if (strToken.contains("-") && strToken.charAt(0) !='-')
                    splitAndAdd(strToken.split("-"));
                    // maybe make a different function and iterate only once
                else putInMap(strToken);


            }

            i++;
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
