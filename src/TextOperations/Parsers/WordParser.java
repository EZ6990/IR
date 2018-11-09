package TextOperations.Parsers;

import TextOperations.Token;

public class WordParser extends AbstractParser {


    @Override
    public void manipulate() {
        int i = 0, size = getTxtSize();
        String s = "";
        Token token;

        while (i < size - 1) {
            s = "";
            token = get(i);
            if (!token.isNumber() && !isFraction(token.toString())) {
                if (token.toString().contains("-") && token.toString().length() > 1)
                    splitAndAdd(token.toString().split("-"));
                else putInMap(token.toString());


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


    private boolean isFraction(String string) {
        if (!string.contains("/") || string.charAt(0) == '/' || string.charAt(string.length() - 1) == '/')
            return false;

        for (int i = 0; i < string.length(); i++)
            if ((string.charAt(i) < '0' || string.charAt(i) > '9') && string.charAt(i) != '/')
                return false;

        return true;

    }
}
