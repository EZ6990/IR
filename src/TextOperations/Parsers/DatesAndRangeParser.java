package TextOperations.Parsers;

import java.util.HashSet;

public class DatesAndRangeParser extends AbstractParser {
    private HashSet<String> dates;


    public DatesAndRangeParser() {
        dates = new HashSet<>();
        dates.add("January");
        dates.add("February");
        dates.add("March");
        dates.add("April");
        dates.add("May");
        dates.add("June");
        dates.add("July");
        dates.add("August");
        dates.add("September");
        dates.add("October");
        dates.add("November");
        dates.add("December");

        dates.add("JANUARY");
        dates.add("FEBRUARY");
        dates.add("MARCH");
        dates.add("APRIL");
        dates.add("MAY");
        dates.add("JUNE");
        dates.add("JULY");
        dates.add("AUGUST");
        dates.add("SEPTEMBER");
        dates.add("OCTOBER");
        dates.add("NOVEMBER");
        dates.add("DECEMBER");

        dates.add("Jan");
        dates.add("Feb");
        dates.add("Mar");
        dates.add("Apr");
        dates.add("Jun");
        dates.add("Jul");
        dates.add("Aug");
        dates.add("Sep");
        dates.add("Oct");
        dates.add("Nov");
        dates.add("Dec");


    }

    @Override
    public void manipulate() {

    }
}
