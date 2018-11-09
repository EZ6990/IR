package TextOperations.Parsers;

import TextOperations.Token;

import java.util.List;

public class NumberParser extends AbstractParser {


    @Override
    public void manipulate() {
        int i = 0;
        int size = getTxtSize();
        String s = "";
        Token token;
        Token nextToken;


        while (i < size - 1) {
            s="";
            token = get(i);
            nextToken = get(i + 1);
            if (token.isNumber()) {

                s = convertNumber(token.toString());
                if (isFraction(nextToken.toString())) {
                    s=s+" "+nextToken.toString();
                    i++;
                } else if (nextToken.toString().equals("Thousand") || nextToken.toString().equals("Million") || nextToken.toString().equals("Billion")) {
                    s = s + nextToken.toString().charAt(0);
                    i++;

                } else if (nextToken.toString().equals("Trillion")) {
                    s = Double.parseDouble(s) * 1000 + "B";
                    i++;
                }
                putInMap(s);
            }

            i++;
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



    private String convertNumber(String string) {

        double num = Double.parseDouble(string);
        if (num >= 1000000000)
            return ("" + num / 1000000000 + "B");
        else if (num >= 1000000)
            return ("" + num / 1000000 + "M");
        else if (num >= 1000)
            return ("" + num / 1000 + "K");

        else return "" + num;
    }


}
