package MapReduce.Parsers;

import MapReduce.TermDocumentInfo;
import TextOperations.Token;
import TextOperations.TokenizedDocument;

import java.util.HashMap;


public class NumberParser extends AbstractParser {


    public NumberParser(HashMap<String, TermDocumentInfo> map, TokenizedDocument doc) {
        super(map,doc);
    }

    @Override
    public void manipulate() {
        int i = 0;
        int size = getTxtSize();
        String s = "";
        Token token;
        Token nextToken;
        String tokenStr,nextTokenStr;

        while (i < size - 1) {
            s = "";
            token = get(i);
            nextToken = get(i + 1);
            tokenStr=token.toString();
            nextTokenStr=nextToken.toString();
            if (isNumber(token)) {

                s = convertNumber(tokenStr);
                if (isFraction(nextTokenStr)) {
                    s = s + " " + nextTokenStr;
                    i++;
                } else if (nextTokenStr.equals("Thousand") || nextTokenStr.equals("Million") || nextTokenStr.equals("Billion")) {
                    s = s + nextTokenStr.charAt(0);
                    i++;

                } else if (nextTokenStr.equals("Trillion")) {
                    s = Double.parseDouble(s) * 1000 + "B";
                    i++;
                }
                putInMap(s);
            }

            i++;
        }


    }

    private boolean isNumber(Token token) {
        if(token.isNumber())
            return true;
        String s=token.toString().replace(",","");
            try {
            Double.parseDouble(s);
        return true;
        }
        catch (Exception e){
            return false;
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
        String s=string.replace(",","");

        double num = Double.parseDouble(s);
        if (num >= 1000000000)
            return ("" + num / 1000000000 + "B");
        else if (num >= 1000000)
            return ("" + num / 1000000 + "M");
        else if (num >= 1000)
            return ("" + num / 1000 + "K");

        else return "" + num;
    }


}
