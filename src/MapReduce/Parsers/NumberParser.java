package MapReduce.Parsers;

import MapReduce.AbstractTermDocumentInfo;
import TextOperations.Token;
import TextOperations.TokenizedDocument;
import java.util.HashMap;


public class NumberParser extends AbstractParser {


    public NumberParser(HashMap<String, AbstractTermDocumentInfo> map, TokenizedDocument doc) {
        super(map, doc);
    }

    @Override
    public void manipulate() {
        int i = 0;
        int size = getTxtSize();
        String s = "";
        Token token;
        Token nextToken = null;
        String tokenStr="", nextTokenStr="";
        Double theNumber;
        boolean isTrillion;
        double valueNumber;
        while (i < size - 1) {

            s = "";
            token = get(i);
            nextToken = get(i + 1);
            tokenStr = token.toString();
            nextTokenStr = nextToken.toString();

            isTrillion = false;

            if ((theNumber = isNumber(token)) != null) {
                valueNumber = theNumber.doubleValue();
                s = "" + valueNumber;
                s=convertNumber(s,false);

                if (isFraction(nextTokenStr) && valueNumber < 1000000) {
                    s = s + " " + nextTokenStr;
                    i++;
                } else {
                    if (nextTokenStr.equals("Thousand") || nextTokenStr.equals("THOUSAND") || nextTokenStr.equals("thousand")) {
                        valueNumber = valueNumber * 1000;
                        i++;

                    } else if (nextTokenStr.equals("Million") || nextTokenStr.equals("MILLION") || nextTokenStr.equals("million")) {
                        valueNumber = valueNumber * 1000000;
                        i++;
                    } else if (nextTokenStr.equals("Billion") || nextTokenStr.equals("BILLION") || nextTokenStr.equals("billion")) {
                        valueNumber = valueNumber * 1000000000;
                        i++;
                    } else if (nextTokenStr.equals("Trillion") || nextTokenStr.equals("TRILLION") || nextTokenStr.equals("trillion")) {
                        valueNumber = valueNumber * 1000000000;
                        isTrillion = true;
                        i++;
                    } else if (valueNumber < 1000 && isFraction(nextTokenStr)) {
                        s = valueNumber +" "+ nextTokenStr;
                        i++;
                    }
                    s=valueNumber+"";
                    s = convertNumber(s, isTrillion);
                }
                putInMap(s);
            } else if (isFraction(tokenStr))
                putInMap(tokenStr);
            i++;
        }
        try {
            if (((theNumber = isNumber(nextToken)) != null)) {
                valueNumber = theNumber.doubleValue();
                s = "" + valueNumber;
                putInMap(convertNumber(s, false));
            } else if (isFraction(nextTokenStr))
                putInMap(tokenStr);
        } catch (Exception e){
            //System.out.println("Document with Text Problem ID: " + document.getID() + " size : " + getTxtSize());
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


    private String convertNumber(String string, Boolean isTrillion) {

        double num = Double.parseDouble(string);
        if (num >= 1000000000) {
            if (isTrillion) {
                num = num / 1000000;

            } else {
                num = num / 1000000000;


            }
            string = num +"";
            if (num % 1 > 0) {
                String s = num + "";
                if (s.indexOf('.') < s.length() - 3)
                    return (s.substring(0, s.indexOf('.') + 3) + "B");
                else
                    return s + "B";
            } else return string.indexOf('.') == -1 ? (string=num+ "") : (string=num+ "").substring(0, string.indexOf('.')) + "B";
        } else if (num >= 1000000) {
            num = num / 1000000;
            string = num +"";
            if (num % 1 > 0) {
                String s = num + "";
                if (s.indexOf('.') < s.length() - 3)
                    return (s.substring(0, s.indexOf('.') + 3) + "M");
                else
                    return s + "M";
            } else return string.substring(0, string.indexOf('.')) + "M";

        } else if (num >= 1000) {
            num = num / 1000;
            string = num +"";
            if (num % 1 > 0) {
                String s = num + "";
                if (s.indexOf('.') < s.length() - 3)
                    return (s.substring(0, s.indexOf('.') + 3) + "K");
                else
                    return s + "K";
            } else return string.substring(0, string.indexOf('.')) + "K";
        } else {
            if (num % 1 > 0) {
                String s = num + "";
                if (s.indexOf('.') < s.length() - 3)
                    return (s.substring(0, s.indexOf('.') + 3));
                else
                    return s;

            } else return string.substring(0, string.indexOf('.'));
        }
    }

}
