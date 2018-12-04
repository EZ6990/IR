package MapReduce.Parsers;

import MapReduce.AbstractTermDocumentInfo;
import TextOperations.Stemmer;
import TextOperations.Token;
import TextOperations.TokenizedDocument;
import java.util.HashMap;

public class PercentAndPriceParser extends AbstractParser {


    public PercentAndPriceParser(HashMap<String, AbstractTermDocumentInfo> map, TokenizedDocument doc, Stemmer stemmer) {
        super(map, doc,stemmer);
    }

    @Override
    public void manipulate() {
        int i = 0, size = getTxtSize();
        String s = "";
        Token token, nextToken;
        String tokenStr, nextTokenStr;

        while (i < size - 1) {
            s = "";
            token = get(i);
            nextToken = get(i + 1);
            tokenStr = token.toString();
            nextTokenStr = nextToken.toString();
            Double theNumber;

            if ((theNumber = isNumber(token)) != null) {
                if (nextTokenStr.equals("percent") || nextTokenStr.equals("percentage")) {
                    i++;
                    s = s + theNumber.doubleValue() + "%";
                } else if (nextTokenStr.equals("Dollars")) {
                    s = s + convertNumber((theNumber.doubleValue() + ""), false) + " Dollars";
                    i++;
                } else if (nextTokenStr.contains("illion") && size >= (i + 3) && get(i + 2).toString().equals("U.S.") && get(i + 3).toString().equals("dollars")) {
                    if (nextTokenStr.equals("billion") || nextTokenStr.equals("Billion") || nextTokenStr.equals("BILLION")) {
                        s = s + convertNumber(((theNumber.doubleValue() * 1000000000) + ""), false) + " Dollars";
                        i = i + 3;
                    } else if (nextTokenStr.equals("million") || nextTokenStr.equals("Million") || nextTokenStr.equals("MILLION")) {
                        s = s + convertNumber(((theNumber.doubleValue() * 1000000) + ""), false) + " Dollars";
                        i = i + 3;
                    } else if (nextTokenStr.equals("trillion") || nextTokenStr.equals("Trillion") || nextTokenStr.equals("TRILLION")) {
                        s = s + convertNumber((theNumber.doubleValue() * 1000000000) + "", true) + " Dollars";
                        i = i + 3;
                    } else if (size >= (i + 2) && get(i + 2).toString().equals("Dollars")) {
                        if (nextTokenStr.equals("bn")) {
                            s = s + convertNumber(((theNumber.doubleValue() * 1000000000) + ""), false) + " Dollars";
                            i = i + 2;
                        } else if (nextTokenStr.equals("m")) {
                            s = s + convertNumber(((theNumber.doubleValue() * 1000000) + ""), false) + " Dollars";
                            i = i + 2;
                        }
                    } else if (isFraction(nextTokenStr) && size >= (i + 2) && get(i + 2).toString().equals("Dollars")) {
                        if (theNumber.doubleValue() > 1000000)
                            s = convertNumber(theNumber + "", false) + nextTokenStr + " Dollars";
                        else
                            s = convertNumberLessthanMill(tokenStr) + nextTokenStr + " Dollars";
                        i+=2;

                    }


                }


            } else if (tokenStr.charAt(tokenStr.length() - 1) == '%' &&
                    (theNumber = isNumber(new Token(tokenStr.substring(0, tokenStr.length() - 1)))) != null) {
                s = theNumber + "%";
            } else if (tokenStr.charAt(0) == '$' && (theNumber = isNumber(new Token(tokenStr.substring(1)))) != null)
                if (nextTokenStr.contains("illion")) {
                    if (nextTokenStr.equals("billion") || nextTokenStr.equals("Billion") || nextTokenStr.equals("BILLION")) {
                        s = s + convertNumber(((theNumber.doubleValue() * 1000000000) + ""), false) + " Dollars";
                        i++;
                    } else if (nextTokenStr.equals("million") || nextTokenStr.equals("Million") || nextTokenStr.equals("MILLION")) {
                        s = s + convertNumber(((theNumber.doubleValue() * 1000000) + ""), false) + " Dollars";
                        i++;
                    }
                } else if (nextTokenStr.equals("trillion") || nextTokenStr.equals("Trillion") || nextTokenStr.equals("TRILLION")) {
                    s = s + convertNumber((theNumber.doubleValue() * 1000000000) + "", true) + " Dollars";
                    i++;
                } else {
                    if (theNumber.doubleValue() > 1000000)
                        s = s + convertNumber(theNumber.doubleValue() + "", false) + " Dollars";
                    else
                        s = s + convertNumberLessthanMill(tokenStr) + " Dollars";
                }
            i++;
        }
    }

    private String convertNumberLessthanMill(String tokenStr) {
        String s = tokenStr;
        while (s.charAt(s.length() - 1) == '.')
            s = s.substring(0, s.length() - 1);
        return s;
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

    //function knows regular numbers and 1,000,000
    private String convertNumber(String s, boolean isTrilion) {
        double d = Double.parseDouble(s);
        if (isTrilion)
            return d / 1000 + " M";
        else if (d > 1000000)
            return d / 1000000 + " M";
        else return d + "";
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
