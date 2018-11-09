package TextOperations.Parsers;

import TextOperations.Token;

import java.util.HashMap;

public class DatesAndRangeParser extends AbstractParser {
    private HashMap<String, String> monthDates;
    private HashMap<String, String> dayDates;

    public DatesAndRangeParser() {
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
        int i = 0;
        int size = getTxtSize();
        String s = "";
        Token token;
        Token nextToken;
        while (i < size - 1) {
            token = get(i);
            nextToken = get(i + 1);
            if (monthDates.containsKey(token.toString())) {
                s = s + monthDates.get(token.toString());
                if (nextToken.isNumber()) {
                    if (dayDates.containsKey(nextToken.toString()))
                        s = s + "-" + dayDates.get(nextToken.toString());

                    if (nextToken.toString().length() < 5 && !nextToken.toString().contains("."))
                        putInMap(convertToYear(nextToken.toString()) + "-" + s.substring(0, 2));
                    i++;
                }
            } else if (token.isNumber()) {
                if ( dayDates.containsKey(token.toString()) && monthDates.containsKey(nextToken.toString()))
                    s = s +monthDates.get(nextToken.toString())+dayDates.get(token.toString());
            }
        }
    }

    private String convertToYear(String string) {
        String sYear = string;
        while (sYear.length() < 4)
            sYear = 0 + sYear;
        return sYear;
    }
}
