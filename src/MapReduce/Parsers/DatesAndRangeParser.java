package MapReduce.Parsers;

import MapReduce.AbstractTermDocumentInfo;
import TextOperations.Token;
import TextOperations.TokenizedDocument;
import java.util.HashMap;

public class DatesAndRangeParser extends AbstractParser {
    private HashMap<String, String> monthDates;
    private HashMap<String, String> dayDates;

    public DatesAndRangeParser(HashMap<String, AbstractTermDocumentInfo> map, TokenizedDocument doc) {
        super(map, doc);
        monthDates = new HashMap<>();
        dayDates = new HashMap<>();

        monthDates.put("January", "01");
        monthDates.put("February", "02");
        monthDates.put("March", "03");
        monthDates.put("April", "04");
        monthDates.put("May", "05");
        monthDates.put("June", "06");
        monthDates.put("July", "07");
        monthDates.put("August", "08");
        monthDates.put("September", "09");
        monthDates.put("October", "10");
        monthDates.put("November", "11");
        monthDates.put("December", "12");

        monthDates.put("JANUARY", "01");
        monthDates.put("FEBRUARY", "02");
        monthDates.put("MARCH", "03");
        monthDates.put("APRIL", "04");
        monthDates.put("MAY", "05");
        monthDates.put("JUNE", "06");
        monthDates.put("JULY", "07");
        monthDates.put("AUGUST", "08");
        monthDates.put("SEPTEMBER", "09");
        monthDates.put("OCTOBER", "10");
        monthDates.put("NOVEMBER", "11");
        monthDates.put("DECEMBER", "12");

        monthDates.put("Jan", "01");
        monthDates.put("Feb", "02");
        monthDates.put("Mar", "03");
        monthDates.put("Apr", "04");
        monthDates.put("Jun", "06");
        monthDates.put("Jul", "07");
        monthDates.put("Aug", "08");
        monthDates.put("Sep", "09");
        monthDates.put("Oct", "10");
        monthDates.put("Nov", "11");
        monthDates.put("Dec", "12");

        for (int i = 1; i < 10; i++)
            dayDates.put("" + i, "0" + i);
        for (int i = 10; i < 32; i++)
            dayDates.put("" + i, "" + i);


    }

    @Override
    public void manipulate() {
        int i = 0, size = getTxtSize();
        String s = "";
        Token token, nextToken;
        String tokenStr, nextTokenStr;
        Double theNumber;
        while (i < size - 1) {
            s = "";
            token = get(i);
            nextToken = get(i + 1);
            tokenStr = token.toString();
            nextTokenStr = nextToken.toString();

            if (tokenStr.contains("-")&& tokenStr.charAt(0)!='-')
                putInMap(tokenStr);

            if ((tokenStr.equals("Between")||tokenStr.equals("between")||tokenStr.equals("BETWEEN")) && getTxtSize() - i >= 4 && isNumber(nextToken)!=null
                    && (get(i + 2).toString().equals("and")|| get(i + 2).toString().equals("AND"))&& isNumber(get(i + 3))!=null) {
                s = s + "between " + isNumber(nextToken).doubleValue() + " and " + isNumber(get(i + 3)).doubleValue();
                i = i + 3;
            } else {
                //first is month
                if (monthDates.containsKey(tokenStr)) {
                    s = s + monthDates.get(tokenStr);
                    //second is a number
                    if ((theNumber=isNumber(nextToken))!=null) {
                        //DD
                        if (dayDates.containsKey(theNumber.intValue()))
                            s = s + "-" + dayDates.get(theNumber.intValue());
                        //YYYY
                        if (nextTokenStr.length() < 5 && !nextTokenStr.contains("."))
                            putInMap(convertToYear(theNumber.intValue()+"") + "-" + s.substring(0, 2));
                        i++;
                    }

                    //first is number
                } else if ((theNumber=isNumber(token))!=null) {
                    //second is month
                    if (dayDates.containsKey(theNumber.intValue()+"") && monthDates.containsKey(nextTokenStr)) {
                        s = s + monthDates.get(nextTokenStr) + dayDates.get(theNumber.intValue()+"");

                    }
                }
            }

            i++;
        }
    }


    /**
     * convet a string that represents a number to YYYY
     *
     * @param string
     * @return string size 4 filled with 0 in the left if needed
     */
    private String convertToYear(String string) {
        String sYear = string;
        while (sYear.length() < 4)
            sYear = "0" + sYear;
        return sYear;
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


}
