package MapReduce.Parsers;

import MapReduce.TermDocumentInfo;
import TextOperations.Token;
import TextOperations.TokenizedDocument;

import java.util.HashMap;

public class PercentAndPriceParser extends AbstractParser {


    public PercentAndPriceParser(HashMap<String, TermDocumentInfo> map, TokenizedDocument doc) {
        super(map,doc);
    }

    @Override
    public void manipulate() {
        int i = 0, size = getTxtSize();
        String s = "";
        Token token, nextToken;
        String tokenStr,nextTokenStr;

        while (i < size - 1) {
            s = "";
            token = get(i);
            nextToken = get(i + 1);
            tokenStr=token.toString();
            nextTokenStr=nextToken.toString();
            if (isNumber(token)) {
                if (nextTokenStr.equals("percent") || nextTokenStr.equals("percentage")) {
                    i++;
                    s = s + tokenStr + "%";
                } else if (nextTokenStr.equals("Dollars")) {
                    s = s + convertNumber(tokenStr) + "Dollars";
                    i++;
                } else if (nextTokenStr.contains("illion") && size >= (i + 3) && get(i + 2).toString().equals("U.S.") && get(i + 3).toString().equals("dollars")) {
                    if (nextTokenStr.contains("billion")) {
                        s = s + convertNumber(tokenStr + "000000000") + "Dollars";
                    i=i+3;
                    }
                    else if (nextTokenStr.contains("million")) {
                        s = s + convertNumber(tokenStr + "000000") + "Dollars";
                        i=i+3;
                    }
                    else if (nextTokenStr.contains("trillion")) {
                        s = s + convertNumber(tokenStr + "000000000000") + "Dollars";
                        i=i+3;
                    }

                }


            }

            else if (tokenStr.contains("%") && (isFraction(tokenStr) || isNumber(token)))
            {
                s=s+tokenStr;
            }

            else if (tokenStr.charAt(0)=='$' && (isFraction(tokenStr) || isNumber(token)))
                if (nextTokenStr.contains("illion")) {
                    if (nextTokenStr.contains("billion")) {
                        s = s + convertNumber(tokenStr.substring(1) + "000000000") + "Dollars";
                        i++;
                    }
                    else if (nextTokenStr.contains("million")){
                        s = s + convertNumber(tokenStr.substring(1) + "0000000") + "Dollars";
                        i++;
                    }
                }
                else{
                    s = s + convertNumber(tokenStr.substring(1)) + "Dollars";
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

    //function knows regular numbers and 1,000,000
    private String convertNumber(String s) {
        try {
            double d = Double.parseDouble(s);
            if (d > 1000000)
                return d / 1000000 + " M ";
            else return d + "";

        } catch (final Exception e) {
            String newString = s.replace(",", "");
            return convertNumber(newString);
        }
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
