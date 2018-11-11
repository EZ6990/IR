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
                } else if (nextToken.toString().equals("Dollars")) {
                    s = s + convertNumber(token.toString()) + "Dollars";
                    i++;
                } else if (nextToken.toString().contains("illion") && size >= (i + 3) && get(i + 2).toString().equals("U.S.") && get(i + 3).toString().equals("dollars")) {
                    if (nextToken.toString().contains("billion")) {
                        s = s + convertNumber(token.toString() + "000000000") + "Dollars";
                    i=i+3;
                    }
                    else if (nextToken.toString().contains("million")) {
                        s = s + convertNumber(token.toString() + "000000") + "Dollars";
                        i=i+3;
                    }
                    else if (nextToken.toString().contains("trillion")) {
                        s = s + convertNumber(token.toString() + "000000000000") + "Dollars";
                        i=i+3;
                    }

                }


            }

            else if (token.toString().contains("%") && (isFraction(token.toString()) || isNumber(token.toString())))
            {
                s=s+token.toString();
            }

            else if (token.toString().charAt(0)=='$' && (isFraction(token.toString()) || isNumber(token.toString())))
                if (nextToken.toString().contains("illion")) {
                    if (nextToken.toString().contains("billion")) {
                        s = s + convertNumber(token.toString().substring(1) + "000000000") + "Dollars";
                        i++;
                    }
                    else if (nextToken.toString().contains("million")){
                        s = s + convertNumber(token.toString().substring(1) + "0000000") + "Dollars";
                        i++;
                    }
                }
                else{
                    s = s + convertNumber(token.toString().substring(1)) + "Dollars";
                }
            i++;
        }
    }

    private boolean isNumber(String s) {
        return false;
    }

    //fubnction knows regular numbers and 1,000,000
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
        if (!string.contains("/") || string.charAt(0) == '/' || string.charAt(string.length() - 1) == '/')
            return false;

        for (int i = 0; i < string.length(); i++)
            if ((string.charAt(i) < '0' || string.charAt(i) > '9') && string.charAt(i) != '/')
                return false;

        return true;

    }
}
