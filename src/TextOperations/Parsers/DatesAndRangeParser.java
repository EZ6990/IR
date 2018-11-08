package TextOperations.Parsers;

import TextOperations.Token;

import java.util.HashMap;
import java.util.HashSet;

public class DatesAndRangeParser extends AbstractParser {
    private HashMap<String, Integer> dates;


    public DatesAndRangeParser() {
        dates = new HashMap<>();

        dates.put("January", new Integer(1));
        dates.put("February", new Integer(2));
        dates.put("March", new Integer(3));
        dates.put("April", new Integer(4));
        dates.put("May", new Integer(5));
        dates.put("June", new Integer(6));
        dates.put("July", new Integer(7));
        dates.put("August", new Integer(8));
        dates.put("September", new Integer(9));
        dates.put("October", new Integer(10));
        dates.put("November", new Integer(11));
        dates.put("December", new Integer(12));

        dates.put("JANUARY", new Integer(1));
        dates.put("FEBRUARY", new Integer(2));
        dates.put("MARCH", new Integer(3));
        dates.put("APRIL", new Integer(4));
        dates.put("MAY", new Integer(5));
        dates.put("JUNE", new Integer(6));
        dates.put("JULY", new Integer(7));
        dates.put("AUGUST", new Integer(8));
        dates.put("SEPTEMBER", new Integer(9));
        dates.put("OCTOBER", new Integer(10));
        dates.put("NOVEMBER", new Integer(11));
        dates.put("DECEMBER", new Integer(12));

        dates.put("Jan", new Integer(1));
        dates.put("Feb", new Integer(2));
        dates.put("Mar", new Integer(3));
        dates.put("Apr", new Integer(4));
        dates.put("Jun", new Integer(6));
        dates.put("Jul", new Integer(7));
        dates.put("Aug", new Integer(8));
        dates.put("Sep", new Integer(9));
        dates.put("Oct", new Integer(10));
        dates.put("Nov", new Integer(11));
        dates.put("Dec", new Integer(12));


    }

    @Override
    public void manipulate() {
//        int i = 0;
//        int size = getTxtSize();
//        String s = "";
//        Token token;
//        Token nextToken;
//        while (i < size - 1) {
//            token = get(i);
//            nextToken = get(i + 1);
//        }
   }
}
