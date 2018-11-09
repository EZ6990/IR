package TextOperations.Parsers;

import TextOperations.Token;

public class PercentAndPriceParser extends AbstractParser {


    @Override
    public void manipulate() {
        int i = 0, size = getTxtSize();
        String s = "";
        Token token, nextToken;

        while (i < size - 1) {
            s = "";
            token = get(i);
            nextToken = get(i + 1);
            if (token.isNumber()) {
                if (nextToken.toString().equals("percent") && nextToken.toString().equals("percentage")) {
                    i++;
                    s = s + token.toString() + "%";
                }

                if ()
            }
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
