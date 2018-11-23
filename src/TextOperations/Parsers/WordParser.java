package TextOperations.Parsers;

import TextOperations.Token;

public class WordParser extends AbstractParser {


    @Override
    public void manipulate() {
        int i = 0, size = getTxtSize(), suffix, prefix;
        String strToken;
        Token token;

        while (i < size - 1) {
            token = get(i);
            strToken = token.toString();
            suffix = strToken.length() - 1;
            prefix = 0;
            if (!token.isNumber() && !isFraction(token.toString())) {
                while (!(prefix < suffix) || !(suffix < 0)) {


                    char cSuff = strToken.charAt(suffix);
                    char cpreff = strToken.charAt(prefix);

                    if (!(cSuff <= 'z' && cSuff >= 'a') && !(cSuff <= 'Z' && cSuff >= 'A'))
                        --suffix;

                    if (!(cpreff <= 'z' && cpreff >= 'a') && !(cpreff <= 'Z' && cpreff >= 'A'))
                        ++prefix;
                }

                strToken = strToken.substring(prefix, suffix + 1);


                if (strToken.contains("-") && strToken.length() > 1)
                    splitAndAdd(token.toString().split("-"));
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
        if (mapContains(s.toUpperCase()) && s.charAt(0) >= 'a') {
            super.putInMap(s);
            mapRemove(s.toUpperCase());
        } else if (s.charAt(0) <= 'Z')
            super.putInMap(s.toUpperCase());

        else super.putInMap(s);
    }

    private boolean isFraction(String string) {
        if (!string.contains("/") || string.charAt(0) == '/' || string.charAt(string.length() - 1) == '/')
            return false;

        for (int i = 0; i < string.length(); i++)
            if ((string.charAt(i) < '0' || string.charAt(i) > '9') && string.charAt(i) != '/')
                return false;

        return true;

    }
}
